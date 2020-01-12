export class Comment {
  id: any;
  username: string;
  bookId: string;
  timestamp: string;
  title: string;
  comment: string;

  constructor(username, id, comment, title) {
    this.id = null;
    this.username = username;
    this.bookId = id;
    this.timestamp = null;
    this.title = title;
    this.comment = comment;
  }
}
