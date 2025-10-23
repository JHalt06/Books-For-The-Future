export interface User {
    username: string;
    basket: number[];
    role: UserRole;
}

export enum UserRole {
    HELPER = "HELPER",
    MANAGER = "MANAGER"
}

