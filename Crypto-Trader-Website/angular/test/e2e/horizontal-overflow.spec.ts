import { test, expect } from '@playwright/test';
import { waitForIdlePage } from '@app/test/utils/playwright-utils';
import { type Page } from 'playwright';

type ScrollableContainerTestCase = {
    endpoint: string;
    pageTitle: string;
};

async function isScrollableContainer(page: Page, scrollSelector: string): Promise<boolean> {
    return await page.evaluate((selector: string) => {
        const container: Element | null = document.querySelector(selector);
        if (!container) {
            return false;
        }
        return container.scrollWidth > container.clientWidth;
    }, scrollSelector);
}

test.describe('Horizontal Scroll Check', () => {
    const testCases: ScrollableContainerTestCase[] = [
        { endpoint: '/', pageTitle: 'home' },
        { endpoint: '/dashboard', pageTitle: 'dashboard' },
    ];
    const scrollSelector: string = '.scroll-container';
    for (const { endpoint, pageTitle } of testCases) {
        test(`should not have horizontal scroll on the ${pageTitle}`, async ({ page }) => {
            await page.goto(endpoint);
            await waitForIdlePage(page);
            const isHorizontalScrollable: boolean = await isScrollableContainer(
                page,
                scrollSelector,
            );
            expect(isHorizontalScrollable).toBe(false);
        });
    }
});
