<template>
  <h1>자유 게시판 - 목록</h1>
  <form @submit.prevent="getBoards(searchCondition.pageNum)">
    <input type="date" v-model="searchCondition.startDate" />
    <input type="date" v-model="searchCondition.endDate" />
    <select v-model="searchCondition.categoryId">
      <option value="-1">전체 카테고리</option>
      <option
        v-for="category in categoryList"
        :value="category.categoryId"
        :key="category.categoryId"
      >
        {{ category.categoryName }}
      </option>
    </select>
    <input type="text" v-model="searchCondition.searchText" required />
    <button type="submit">검색</button>
  </form>

  <div>
    <span>총 {{ boardCount }}건</span>
  </div>
  <table>
    <tr>
      <th>카테고리</th>
      <th>File</th>
      <th>제목</th>
      <th>작성자</th>
      <th>조회수</th>
      <th>등록 일시</th>
      <th>수정 일시</th>
    </tr>
    <tr v-for="board in boardList" :key="board.boardId">
      <td>{{ board.categoryName }}</td>
      <td v-if="board.boardIdInFile">O</td>
      <td v-else></td>
      <td style="cursor: pointer" @click="goToView(board.boardId)">
        {{ board.title }}
      </td>
      <td>{{ board.userName }}</td>
      <td>{{ board.views }}</td>
      <td>{{ parseStringFormat(board.createdAt, 'YYYY-MM-DD HH:mm') }}</td>
      <td v-if="board.editedAt != null">
        {{ parseStringFormat(board.editedAt, 'YYYY-MM-DD HH:mm') }}
      </td>
      <td v-else>-</td>
    </tr>
  </table>
  <nav>
    <span v-for="page in numOfPages" :key="page" style="margin-right: 3px">
      <a @click="getBoards(page)">{{ page }}</a>
    </span>
  </nav>
  <button @click="goToWrite">등록</button>
</template>

<script>
import { ref, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { getBoardList } from '@/api/boardService';
import { getCategoryList } from '@/api/categoryService';
import { parseStringFormat } from '@/pages/utils/stringUtils';

export default {
  setup() {
    // router
    const router = useRouter();
    const route = useRoute();
    // 페이지에 사용될 변수들
    const boardList = ref([]);
    const boardCount = ref(0);
    const categoryList = ref([]);
    // 검색 조건
    const searchCondition = ref({
      startDate: route.query.startDate || '',
      endDate: route.query.endDate || '',
      categoryId: Number(route.query.category) || -1,
      searchText: route.query.searchText || '',
      pageNum: Number(route.query.pageNum) || 1,
    });

    // 페이지네이션
    const pageSize = ref(10);
    const numOfPages = computed(() => {
      return Math.ceil(boardCount.value / pageSize.value);
    });

    /**
     * 페이지에 사용 될 boardList 가져오기
     * @param pageNum
     */
    const getBoards = async (pageNum) => {
      searchCondition.value.pageNum = pageNum;
      const res = await getBoardList(searchCondition.value, pageSize.value);
      searchCondition.value = res.searchCondition;
      boardCount.value = res.totalCount;
      boardList.value = res.boardList;
    };
    getBoards(searchCondition.value.pageNum);

    /**
     * 검색창에 사용 될 CategoryList 가죠오기
     */
    const getCategories = async () => {
      const res = await getCategoryList();
      categoryList.value = res;
    };
    getCategories();

    /**
     * 게시판 - 보기 페이지로 이동
     * @param boardId
     */
    const goToView = (boardId) => {
      router.push({
        name: 'View',
        params: {
          id: boardId,
        },
        query: {
          startDate: parseStringFormat(
            searchCondition.value.startDate,
            'YYYY-MM-DD',
          ),
          endDate: parseStringFormat(
            searchCondition.value.endDate,
            'YYYY-MM-DD',
          ),
          categoryId: searchCondition.value.categoryId,
          searchText: searchCondition.value.searchText,
          pageNum: searchCondition.value.pageNum,
        },
      });
    };

    /**
     * 게시판 - 등록 페이지 이동
     */
    const goToWrite = () => {
      router.push({
        name: 'Write',
        query: {
          startDate: parseStringFormat(
            searchCondition.value.startDate,
            'YYYY-MM-DD',
          ),
          endDate: parseStringFormat(
            searchCondition.value.endDate,
            'YYYY-MM-DD',
          ),
          categoryId: searchCondition.value.categoryId,
          searchText: searchCondition.value.searchText,
          pageNum: searchCondition.value.pageNum,
        },
      });
    };

    return {
      boardList,
      categoryList,
      boardCount,
      numOfPages,
      searchCondition,
      goToView,
      goToWrite,
      parseStringFormat,
      getBoards,
    };
  },
};
</script>

<style></style>
