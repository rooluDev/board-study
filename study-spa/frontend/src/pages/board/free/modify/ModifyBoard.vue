<template>
  <h1>게시판 - 수정</h1>
  <div v-if="board != null">
    <span>카테고리</span>
    <p>{{ board.categoryName }}</p>
    <span>등록일시</span>
    <p>{{ parseStringFormat(board.createdAt, 'YYYY-MM-DD HH:mm') }}</p>
    <span>수정 일시</span>
    <p v-if="board.editedAt != null">
      {{ parseStringFormat(board.editedAt, 'YYYY-MM-DD HH:mm') }}
    </p>
    <p v-else>-</p>
    <span>조회수</span>
    <p>{{ board.views }}</p>
    <form @submit.prevent="updateBoard_">
      <span>작성자</span>
      <input
        type="text"
        v-model="board.userName"
        minlength="3"
        maxlength="4"
        required
      />
      <br />
      <span>비밀번호</span>
      <input
        type="password"
        v-model="password"
        minlength="4"
        maxlength="15"
        required
      />
      <br />
      <span>제목</span>
      <input
        type="text"
        v-model="board.title"
        minlength="4"
        maxlength="99"
        required
      />
      <br />
      <span>내용</span>
      <input
        type="text"
        v-model="board.content"
        minlength="4"
        maxlength="1999"
        required
      />
      <br />
      <span>파일 첨부</span>
      <div v-for="(file, index) in fileList">
        <span>{{ file.originalName }}</span>
        <button @click.stop="selectedFile(file.fileId, index)">X</button>
      </div>
      <br />
      <button type="submit">저장</button>
    </form>
    <button @click="goToView">취소</button>
  </div>
</template>
<script>
import { useRouter, useRoute } from 'vue-router';
import { ref } from 'vue';
import { parseStringFormat } from '@/pages/utils/stringUtils';
import { getBoard, updateBoard } from '@/api/boardService';
import { getFileList, deleteFile } from '@/api/fileService';

export default {
  setup() {
    const router = useRouter();
    const route = useRoute();
    // 검색조건 저장
    const boardId = route.query.boardId;
    const startDate = route.query.startDate;
    const endDate = route.query.endDate;
    const categoryId = route.query.categoryId;
    const searchText = route.query.searchText;
    const pageNum = route.query.pageNum;
    // 페이지에 쓰는 데이터 설정
    const board = ref(null);
    const fileList = ref([]);
    // input data
    const password = ref('');
    const deletedFilesList = ref([]);

    /**
     * 게시물 데이터 GET
     * @returns {Promise<void>}
     */
    const getBoard_ = async () => {
      const res = await getBoard(boardId, 'modify');
      board.value = res;
    };
    getBoard_();

    /**
     * 파일 리스트 GET
     * @returns {Promise<void>}
     */
    const getFileList_ = async () => {
      const res = await getFileList(boardId);
      fileList.value = res;
    };
    getFileList_();

    /**
     * 게시물 수정 PUT
     * @returns {Promise<void>}
     */
    const updateBoard_ = async () => {
      const res = await updateBoard(board.value, password.value);
      if (res.errorCode) {
        alert('password incorrect');
        return;
      }
      // 삭제 할 파일리스트 반복문 돌면서 삭제
      for (const file of deletedFilesList.value) {
        await deleteFile(file.fileId);
      }
      // 게시판 - 보기 페이지 이동
      goToView();
    };

    /**
     * 파일 X 버튼 누르면 fileId 저장, 파일 리스트에서 삭제
     * @param fileId
     * @param index
     */
    const selectedFile = (fileId, index) => {
      deletedFilesList.value.push(fileId);
      fileList.value.splice(index, 1);
    };

    const goToView = () => {
      router.push({
        name: 'View',
        params: {
          id: boardId,
        },
        query: {
          startDate: startDate,
          endDate: endDate,
          category: categoryId,
          searchText: searchText,
          pageNum: pageNum,
        },
      });
    };
    return {
      board,
      fileList,
      password,
      goToView,
      parseStringFormat,
      updateBoard_,
      selectedFile,
    };
  },
};
</script>
<style></style>
