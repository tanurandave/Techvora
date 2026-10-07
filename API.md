# Techvora API Documentation

Base URL: `/api/v1`

## 1. Authentication (`/api/v1/auth`)

- `POST /register`: Register a new user.
- `POST /login`: Authenticate and receive a JWT.
- `POST /refresh`: Refresh the JWT token.
- `POST /logout`: Invalidate the current session.

## 2. Public Blog (`/api/v1/articles`)

- `GET /`: Retrieve a paginated list of published articles.
- `GET /{slug}`: Retrieve a single article by its unique slug.

## 3. Search (`/api/v1/search`)

- `GET /?q={query}`: Search articles by keyword (title or content).

## 4. Admin Articles (`/api/v1/admin/articles`)

- `POST /`: Create a new article draft.
- `PUT /{id}`: Update an existing article.
- `DELETE /{id}`: Archive/Delete an article.

## 5. Media Library (`/api/v1/admin/media`)

- `POST /upload`: Upload a multipart file to object storage and return the public URL.

## 6. Bookmarks & Reading Progress (`/api/v1/bookmarks` | `/api/v1/progress`)

- `GET /bookmarks`: Retrieve user's bookmarked articles.
- `POST /bookmarks/{articleId}`: Bookmark an article.
- `DELETE /bookmarks/{articleId}`: Remove a bookmark.
- `POST /progress/{articleId}`: Update reading progress percentage.

## 7. Newsletter (`/api/v1/newsletter`)

- `POST /subscribe`: Subscribe an email to the newsletter.

## 8. Admin & Analytics (`/api/v1/admin/analytics` | `/api/v1/admin/audit-logs`)

- `GET /analytics`: Retrieve platform metrics (users, views).
- `GET /audit-logs`: Retrieve paginated system audit logs.

*Note: All API responses conform to the `ApiResponse<T>` wrapper structure.*
