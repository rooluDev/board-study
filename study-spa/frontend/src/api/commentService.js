import axios from 'axios';

/**
 * GET /api/comment/boardId
 * @param boardId
 * @returns {Promise<*>}
 */
export async function getCommentList(boardId) {
  const res = await axios.get(`/api/comment/${boardId}`);
  return res.data.body;
}

/**
 * POST /api/comment
 * @param boardId
 * @param comment
 * @returns {Promise<*>}
 */
export async function createComment(boardId, comment) {
  const res = await axios.post(`/api/comment`, {
    boardId: boardId,
    comment: comment,
  });
  return res.data.body;
}
