import { api } from '@/api/apiConfig';

/**
 * GET /api/comments/boardId
 * 게시물에 있는 댓글 리스트 가져오기
 *
 * @param boardId pk
 * @returns {Promise<any>}
 */
export const fetchGetCommentList = async (boardId) => {
  const res = await api.get(`/comments/${boardId}`);

  return res.data;
};

/**
 * POST /api/comment
 * 댓글 등록
 *
 * @param boardId pk
 * @param comment 내용
 * @returns {Promise<void>}
 */
export const fetchAddComment = async (boardId, comment) => {
  await api.post('/comment', {
    comment,
    boardId,
  });
};
