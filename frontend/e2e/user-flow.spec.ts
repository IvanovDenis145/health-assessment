import { test, expect, Page } from '@playwright/test';

// ---------- утилиты ----------

// ---------- утилиты ----------

function uniqueEmail() {
    return `u${Date.now()}@t.com`;
}

// Новая функция для уникального телефона (11 цифр, начинается с 79)
function uniquePhoneNumber() {
    return `79${Date.now().toString().slice(-9)}`;
}

async function registerUser(
    page: Page,
    userData: { fio: string; email: string; password: string; age?: number; phoneNumber?: string }
) {
    await page.goto('/register');
    await page.fill('input[name="fio"]', userData.fio);
    await page.fill('input[name="email"]', userData.email);
    await page.fill('input[name="password"]', userData.password);
    if (userData.age) await page.fill('input[name="age"]', String(userData.age));

    // Если телефон не передан, генерируем уникальный автоматически!
    const phone = userData.phoneNumber || uniquePhoneNumber();
    await page.fill('input[name="phoneNumber"]', phone);

    let dialogMessage = '';
    page.once('dialog', async (dialog) => {
        dialogMessage = dialog.message();
        await dialog.accept();
    });

    await page.click('button[type="submit"]');

    try {
        await Promise.race([
            page.waitForURL(/\/login$/, { timeout: 5000 }),
            page.locator('.alert-danger').waitFor({ state: 'visible', timeout: 5000 }),
        ]);
    } catch (e) {
        throw new Error('Регистрация зависла: нет ни редиректа, ни ошибки');
    }

    if (page.url().includes('/register')) {
        const errorText = await page.locator('.alert-danger').textContent();
        throw new Error(`Бэкенд вернул ошибку при регистрации: ${errorText}`);
    }

    expect(dialogMessage).toMatch(/Регистрация успешна/i);
}

async function loginUser(page: Page, email: string, password: string) {
    await page.goto('/login');
    await page.fill('#email', email);
    await page.fill('#password', password);
    await page.click('button[type="submit"]');
    await expect(page).toHaveURL(/\/dashboard$/);
}

async function addIndicatorValue(
    page: Page,
    groupKey: string,
    indicatorTitle: string,
    fieldValues: Record<string, string | number>
) {
    await page.goto(`/indicators/${groupKey}`);

    // Карточка конкретного показателя внутри группы
    const card = page.locator('.custom-card').filter({
        has: page.getByRole('heading', { level: 4, name: indicatorTitle, exact: true }),
    });
    await expect(card).toBeVisible();

    for (const [name, val] of Object.entries(fieldValues)) {
        await card.locator(`input[name="${name}"]`).fill(String(val));
    }
    await card.getByRole('button', { name: 'Добавить', exact: true }).click();

    // Таблица перезагрузилась с сервера — «Нет данных» должно исчезнуть
    await expect(card.locator('tbody')).not.toContainText('Нет данных');
}

// ---------- тесты ----------

test('1. Регистрация нового пользователя и редирект на /login', async ({ page }) => {
    const email = uniqueEmail();
    await registerUser(page, { fio: 'E2E Тестов', email, password: 'password123', age: 30 });
    await expect(page.getByRole('heading', { name: 'Вход в систему' })).toBeVisible();
});

test('2. Вход с неверными данными показывает ошибку', async ({ page }) => {
    await page.goto('/login');
    await page.fill('#email', 'nonexistent_user@test.com');
    await page.fill('#password', 'wrong-password');
    await page.click('button[type="submit"]');

    const error = page.locator('.alert-danger');
    await expect(error).toBeVisible();
    await expect(error).not.toBeEmpty();
    await expect(page).toHaveURL(/\/login$/); // остаёмся на странице логина
});

test('3. Успешный вход и отображение карточки клиента', async ({ page }) => {
    const email = uniqueEmail();
    await registerUser(page, { fio: 'Иванов Иван', email, password: 'password123', age: 30 });
    await loginUser(page, email, 'password123');

    await expect(page.getByRole('heading', { name: 'Карточка клиента' })).toBeVisible();
    await expect(page.getByText('Иванов Иван')).toBeVisible();
    await expect(page.getByText(email)).toBeVisible();
});

test('4. Добавление показателя роста и его появление в таблице', async ({ page }) => {
    const email = uniqueEmail();
    await registerUser(page, { fio: 'Петров Пётр', email, password: 'password123', age: 28 });
    await loginUser(page, email, 'password123');

    await addIndicatorValue(page, 'anthropometric', 'Рост', { value: 180 });

    const heightsCard = page.locator('.custom-card').filter({
        has: page.getByRole('heading', { level: 4, name: 'Рост', exact: true }),
    });
    await expect(heightsCard.locator('tbody')).toContainText('180');
});

test('5. Полный сценарий: заполнение всех обязательных показателей → оценка → отчёт', async ({ page }) => {
    const email = uniqueEmail();
    await registerUser(page, { fio: 'Сидоров Сидор', email, password: 'password123', age: 35 });
    await loginUser(page, email, 'password123');

    // 7 обязательных категорий из DashboardPage.REQUIRED_CATEGORIES
    await addIndicatorValue(page, 'anthropometric', 'Рост',                  { value: 175 });
    await addIndicatorValue(page, 'anthropometric', 'Вес',                   { value: 70 });
    await addIndicatorValue(page, 'functional',    'Артериальное давление',  { upperPressure: 120, lowerPressure: 80 });
    await addIndicatorValue(page, 'functional',    'ЧСС (Пульс)',            { value: 72 });
    await addIndicatorValue(page, 'lab',           'Уровень глюкозы',        { value: '5.5' });
    await addIndicatorValue(page, 'mental',        'Уровень стресса',        { value: 3 });
    await addIndicatorValue(page, 'subjective',    'Оценка самочувствия',    { value: 8 });

    // Дашборд должен разблокировать оценку
    await page.goto('/dashboard');
    await expect(page.getByText('Все данные заполнены!')).toBeVisible();

    const assessBtn = page.getByRole('button', { name: 'Оценить здоровье' });
    await expect(assessBtn).toBeEnabled();
    await assessBtn.click();

    // Переход на отчёт и отрисовка
    await expect(page).toHaveURL(/\/report$/);
    await expect(
        page.getByRole('heading', { name: /Отчёт об оценке состояния здоровья/i })
    ).toBeVisible();
    await expect(page.getByText(/Пациент:\s*Сидоров Сидор/)).toBeVisible();
});