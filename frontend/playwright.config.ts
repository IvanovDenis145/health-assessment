import { defineConfig } from '@playwright/test';

export default defineConfig({
    testDir: './e2e',
    timeout: 60_000,
    expect: { timeout: 10_000 },
    fullyParallel: false,          // тесты регистрируют пользователей — пусть идут по очереди
    retries: 0,
    reporter: [['html', { outputFolder: 'playwright-report' }], ['list']],
    use: {
        baseURL: 'http://localhost:5173',
        trace: 'on-first-retry',
        screenshot: 'only-on-failure',
    },
});