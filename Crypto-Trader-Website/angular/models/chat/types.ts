export type ChatMessage = {
    role: 'user' | 'assistant';
    content: string;
    timestamp: Date;
};

export type ChatConversation = {
    id: string;
    title: string;
    messages: ChatMessage[];
    updatedAt: Date;
};
