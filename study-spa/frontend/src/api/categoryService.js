import axios from 'axios';

/**
 * GET /api/categories
 * @returns {Promise<*>}
 */
export async function getCategoryList() {
  const res = await axios.get(`/api/categories`);
  return res.data.body;
}
