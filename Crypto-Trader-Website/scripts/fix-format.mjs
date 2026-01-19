import { readFileSync, writeFileSync } from 'fs';
import { join } from 'path';
import { globSync } from 'glob';

const files = globSync('angular/**/*.html', { cwd: process.cwd() });

files.forEach((file) => {
    const filePath = join(process.cwd(), file);
    let content = readFileSync(filePath, 'utf8');

    // Remove space before self-closing tag: <app /> -> <app/>
    const newContent = content.replace(/(\S)\s+\/>/g, '$1/>');

    if (content !== newContent) {
        writeFileSync(filePath, newContent, 'utf8');
        console.log(`Fixed formatting in ${file}`);
    }
});
