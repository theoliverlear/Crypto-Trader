import { test, expect, type Locator } from '@playwright/test';
import { PageEndpoints } from '../../models/PageEndpoints';
import { waitForIdlePage } from '@app/test/utils/playwright-utils';

test.describe('Console Loading', () => {
    let navConsoleElement: Locator
    test.beforeEach(async ({ page }) => {
        await page.goto(PageEndpoints.Home);
        await waitForIdlePage(page);
        navConsoleElement = page.locator('nav-console').first();
    });

    test('should load the console page from other pages', async ({ page }) => {
        await navConsoleElement.click();

        await waitForIdlePage(page);

        const welcomeMessage: Locator = page.locator('text=Welcome to the Crypto Trader console!').first();
        await expect(welcomeMessage).toBeVisible();
    });

    test('should load after pop-back navigation', async ({ page }) => {
        await navConsoleElement.click();

        await waitForIdlePage(page);

        const welcomeMessage: Locator = page
            .locator('text=Welcome to the Crypto Trader console!')
            .first();
        await expect(welcomeMessage).toBeVisible();

        await page.goBack();

        await waitForIdlePage(page);

        await navConsoleElement.click();

        await waitForIdlePage(page);

        await expect(welcomeMessage).toBeVisible();
    });
});