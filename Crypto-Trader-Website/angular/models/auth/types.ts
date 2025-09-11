export type LoginRequest = {
    username: string;
    password: string;
};

export type SignupRequest = {
    username: string;
    email: string;
    password: string;
};

export type AuthResponse = {
    authorized: boolean;
};