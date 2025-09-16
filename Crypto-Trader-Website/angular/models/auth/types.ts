export type LoginRequest = {
    email: string;
    password: string;
};

export type SignupRequest = {
    email: string;
    password: string;
};

export type AuthResponse = {
    authorized: boolean;
};