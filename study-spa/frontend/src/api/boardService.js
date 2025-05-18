import { api } from '@/api/apiConfig';
import { parseToQueryString } from '@/pages/utils/utils';

/**
 * GET /api/boards
 * 검색조건으로 게시물 리스트 가져오기
 *
 * @param searchCondition 검색조건
 * @returns {Promise<any>}
 */
export const fetchGetBoardList = async (searchCondition) => {
  const queryString = parseToQueryString(searchCondition);
  const res = await api.get(`/boards${queryString}`);

  return res.data;
};

/**
 * GET /api/board/boardId
 * 단일 게시판 데이터 가져오기
 *
 * @param boardId pk
 * @returns {Promise<*>}
 */
export const fetchGetBoard = async (boardId) => {
  const res = await api.get(`/board/${boardId}`);

  if (res.data.errorCode) {
    throw new Error();
  }
  return res.data;
};

/**
 * POST /api/board
 * 게시판 추가
 *
 * @param formData formData
 * @returns {Promise<void>}
 */
export const fetchPostBoard = async (formData) => {
  await api.post(`/board`, formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });
};

/**
 * PUT /api/board/boardId
 * 게시판 수정
 *
 * @param boardId pk
 * @param formData formData
 * @returns {Promise<void>}
 */
export const fetchEditBoard = async (boardId, formData) => {
  await api.put(`/board/${boardId}`, formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });
};

/**
 * PATCH /api/board/boardId/increase-view
 * 조회수 1 증가
 *
 * @param boardId pk
 * @returns {Promise<void>}
 */
export const fetchAddView = async (boardId) => {
  await api.patch(`/board/${boardId}/increase-view`);
};

/**
 * DELETE /api/board/boardId
 * 게시물 삭제
 *
 * @param boardId pk
 * @returns {Promise<void>}
 */
export const fetchDeleteBoard = async (boardId) => {
  await api.delete(`/board/${boardId}`);
};

/**
 * POST /api/password-check
 * 비밀번호 확인
 *
 * @param formData formData (boardId, enteredPassword)
 * @returns {Promise<void>}
 */
export const fetchCheckPassword = async (formData) => {
  await api.post(`/board/password-check`, formData, {
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
    },
  });
};
