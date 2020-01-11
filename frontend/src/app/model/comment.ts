export class Comment {
  id: any;
  username: string;
  bookId: string;
  timestamp: string;
  comment: string;

  constructor(username, id, comment) {
    this.id = null;
    this.username = username;
    this.bookId = id;
    this.timestamp = null;
    this.comment = comment;
  }
}
