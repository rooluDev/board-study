import { api } from '@/api/apiConfig';

/**
 * GET /api/categories
 * 카테고리 리스트 가져오기
 *
 * @returns {Promise<any>}
 */
export const fetchGetCategoryList = async () => {
  const res = await api.get(`/categories`);
  return res.data;
};
