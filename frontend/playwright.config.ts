import { defineConfig } from '@playwright/test';

export default defineConfig({
    testDir: './e2e',
    timeout: 60_000,
    expect: { timeout: 10_000 },
    fullyParallel: false,
    retries: 0,
    reporter: [['html', { outputFolder: 'playwright-report' }], ['list']],
    use: {
        baseURL: 'http://localhost:5173',
        trace: 'on-first-retry',
        screenshot: 'only-on-failure',
    },
    // --- ДОБАВИТЬ БЛОК webServer ---
    webServer: [
        {
            // Запуск бэкенда (Spring Boot)
            command: 'cd ../backend && ./gradlew bootRun',
            url: 'http://localhost:8080/actuator/health', // Ждем, пока бэкенд поднимется
            timeout: 180 * 1000, // Ждем до 3 минут (сборка Spring Boot в CI может быть долгой)
            reuseExistingServer: !process.env.CI, // Локально используем уже запущенный сервер
            stdout: 'ignore',
            stderr: 'pipe',
        },
        {
            // Запуск фронтенда (Vite)
            command: 'npm run dev',
            url: 'http://localhost:5173',
            timeout: 60 * 1000,
            reuseExistingServer: !process.env.CI,
        }
    ]
});