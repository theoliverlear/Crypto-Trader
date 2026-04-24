import { defineConfig, devices } from '@playwright/test';

export default defineConfig({
    testDir: './angular/test/e2e',
    fullyParallel: true,
    forbidOnly: !!process.env.CI,
    testMatch: '**/*.spec.ts',
    retries: process.env.CI ? 2 : 0,
    workers: process.env.CI ? 1 : undefined,
    reporter: [['html', { open: 'never' }]],
    use: {
        baseURL: 'http://localhost:4200',
        trace: 'on-first-retry',
    },
    projects: [
        {
            name: 'chromium',
            use: { ...devices['Desktop Chrome'] },
        },
    ],
});
