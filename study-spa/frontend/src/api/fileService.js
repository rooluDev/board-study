import axios from 'axios';

/**
 * GET /api/file/fileId/download
 *
 * @param fileId
 * @returns {Promise<any>}
 */
export async function downloadFile(fileId) {
  const res = await axios.get(`/api/file/${fileId}/download`, {
    responseType: 'blob',
  });
  return res.data;
}

/**
 * GET /api/files/boardId
 *
 * @param boardId
 * @returns {Promise<*>}
 */
export async function getFileList(boardId) {
  const res = await axios.get(`/api/files/${boardId}`);
  return res.data.body;
}

/**
 * DELETE /api/file/fileId
 *
 * @param fileId
 * @returns {Promise<*>}
 */
export async function deleteFile(fileId) {
  const res = await axios.delete(`/api/file/${fileId}`);
  return res.data.body;
}
