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

/**
 * 검색조건을 쿼리스트링으로 변환
 *
 * @param searchCondition 검색조건
 * @returns {string} 쿼리스트링
 */
export const parseToQueryString = (searchCondition) => {
  const queryString = `?startDate=${searchCondition.startDate}&endDate=${searchCondition.endDate}&categoryId=${searchCondition.categoryId}
    &searchText=${searchCondition.searchText}&pageNum=${searchCondition.pageNum}`;

  return queryString;
};
