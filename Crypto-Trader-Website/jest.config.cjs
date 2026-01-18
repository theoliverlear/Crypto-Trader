/**
 * Jest configuration for Crypto-Trader-Website
 * - Works with TypeScript source under ./angular/**
 * - Compatible with package.json "type": "module" via CommonJS config file and ts-jest useESM
 */

module.exports = {
    preset: 'jest-preset-angular',
    setupFilesAfterEnv: ['<rootDir>/setup-jest.ts'],
    globalSetup: 'jest-preset-angular/global-setup',
    testEnvironment: 'node',
    roots: ['<rootDir>/angular'],
    testMatch: ['**/?(*.)+(spec|test).ts'],
    moduleFileExtensions: ['ts', 'js', 'json'],
    transform: {
        '^.+\\.ts$': [
            'ts-jest',
            {
                useESM: true,
                tsconfig: '<rootDir>/tsconfig.json',
                isolatedModules: true
            }
        ]
    },
    extensionsToTreatAsEsm: ['.ts'],
    moduleNameMapper: {
        // Map CSS and asset imports to identity-obj-proxy or a stub if needed later
    },
    collectCoverage: true,
    coverageDirectory: 'coverage',
    coverageReporters: ['html', 'text', 'lcov'],
    collectCoverageFrom: [
        'src/app/**/*.ts',
        '!src/app/**/*.spec.ts',
        '!src/app/**/*.module.ts',
        '!src/environments/**'
    ],
    coverageThreshold: {
        global: {
            statements: 80,
            branches: 70,
            functions: 80,
            lines: 80
        }
    }
};
