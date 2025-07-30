export type MessageType = 'success' | 'warning' | 'error' | 'info';

export interface FeedbackMessage {
  type: MessageType;
  text: string;
}
