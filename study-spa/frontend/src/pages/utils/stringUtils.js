import moment from 'moment';

/**
 * 날짜 데이터 스트링 포맷으로 파싱
 * @param timestamp
 * @param format
 * @returns {string}
 */
export const parseStringFormat = (timestamp, format) => {
  return moment(timestamp).format(format);
};
