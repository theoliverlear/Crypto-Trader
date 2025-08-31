/**
 * Jest configuration for Crypto-Trader-Website
 * - Works with TypeScript source under ./angular/**
 * - Compatible with package.json "type": "module" via CommonJS config file and ts-jest useESM
 */

module.exports = {
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
  collectCoverageFrom: [
    'angular/**/*.{ts,js}',
    '!angular/**/*.spec.ts',
    '!angular/**/index.ts'
  ],
  coverageDirectory: '<rootDir>/coverage-jest',
};
