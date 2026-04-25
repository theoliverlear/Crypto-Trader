import { type Page } from 'playwright';

/** Waits until a page's network activity is complete before proceeding.
 *
 * @param page
 */
export async function waitForIdlePage(page: Page): Promise<void> {
    await page.waitForLoadState('networkidle');
}
