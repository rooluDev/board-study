import { api } from '@/api/apiConfig';

/**
 * GET /api/file/fileId/download
 * 파일 리소스 가져오기
 *
 * @param fileId pk
 * @returns {Promise<any>}
 */
export const fetchGetFileResource = async (fileId) => {
  const res = await api.get(`file/${fileId}/download`, {
    responseType: 'blob',
  });

  return res.data;
};
