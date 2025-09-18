export type LoginRequest = {
    email: string;
    password: string;
};

export type SignupRequest = {
    email: string;
    password: string;
};

export type PossibleToken = string | null;

export type PersistMethod = 'memory' | 'session' | 'local';

export type AuthResponse = {
    authorized: boolean;
    token?: string;
};