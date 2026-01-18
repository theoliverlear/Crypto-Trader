import eslint from '@eslint/js';
import tseslint from 'typescript-eslint';
import prettier from 'eslint-config-prettier';
import globals from 'globals';

export default tseslint.config(
    eslint.configs.recommended,
    ...tseslint.configs.recommended,
    {
        files: ['**/*.cjs', 'scripts/**/*.mjs', 'eslint.config.js', 'prettier.config.js'],
        languageOptions: {
            globals: {
                ...globals.node,
            },
        },
    },
    {
        files: ['**/*.ts'],
        languageOptions: {
            parserOptions: {
                projectService: true,
            },
        },
        rules: {
            'prefer-const': 'error',
            'max-len': ['error', {code: 100, ignoreUrls: true}],
            semi: ['error', 'always'],
            indent: ['error', 4, {SwitchCase: 1}],
            quotes: ['warn', 'single', {avoidEscape: true, allowTemplateLiterals: true}],
            'no-multiple-empty-lines': ['error', {max: 2, maxEOF: 1}],
            'object-curly-spacing': ['error', 'never'],
            'arrow-parens': ['error', 'as-needed'],
            '@typescript-eslint/explicit-function-return-type': [
                'warn',
                {
                    allowExpressions: true,
                    allowTypedFunctionExpressions: true,
                },
            ],
            '@typescript-eslint/no-explicit-any': 'error',
            '@typescript-eslint/no-unsafe-assignment': 'warn',
            '@typescript-eslint/no-unsafe-call': 'warn',
            '@typescript-eslint/no-unsafe-member-access': 'warn',
            '@typescript-eslint/no-unsafe-return': 'warn',
            '@typescript-eslint/no-floating-promises': 'error',
            '@typescript-eslint/await-thenable': 'error',
            '@typescript-eslint/no-misused-promises': 'error',
            '@typescript-eslint/no-non-null-assertion': 'warn',
            '@typescript-eslint/no-unnecessary-condition': 'warn',
            '@typescript-eslint/member-ordering': 'off',
            '@typescript-eslint/naming-convention': [
                'error',
                {
                    selector: 'default',
                    format: ['camelCase'],
                },
                {
                    selector: 'variable',
                    format: ['camelCase', 'UPPER_CASE'],
                },
                {
                    selector: 'parameter',
                    format: ['camelCase'],
                    leadingUnderscore: 'allow',
                },
                {
                    selector: 'memberLike',
                    modifiers: ['private'],
                    format: ['camelCase'],
                },
                {
                    selector: 'typeLike',
                    format: ['PascalCase'],
                },
                {
                    selector: 'property',
                    modifiers: ['readonly'],
                    format: ['camelCase', 'PascalCase'],
                },
                {
                    selector: 'enumMember',
                    format: ['UPPER_CASE'],
                },
            ],
            '@typescript-eslint/no-unused-vars': 'warn',
            'space-before-function-paren': [
                'error',
                {
                    anonymous: 'always',
                    named: 'never',
                    asyncArrow: 'always',
                },
            ],
        },
    },
    prettier,
);
