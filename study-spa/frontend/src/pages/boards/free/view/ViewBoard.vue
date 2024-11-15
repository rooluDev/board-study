<template>
  <h1>게시판 - 보기</h1>
  <div v-if="board != null">
    <span>글쓴이</span>
    <p>{{ board.userName }}</p>
    <span>등록일시</span>
    <p>{{ parseStringFormat(board.createdAt, 'YYYY-MM-DD HH:mm') }}</p>
    <span>수정일시</span>
    <p v-if="board.editedAt != null">
      {{ parseStringFormat(board.editedAt, 'YYYY-MM-DD HH:mm') }}
    </p>
    <p v-else>-</p>
    <span>카테고리</span>
    <p>{{ board.categoryName }}</p>
    <span>제목</span>
    <p>{{ board.title }}</p>
    <span>조회수</span>
    <p>{{ board.views }}</p>
    <span>내용</span>
    <p>{{ board.content }}</p>
    <p>첨부파일</p>
    <p
      v-if="fileList != null"
      v-for="file in fileList"
      :key="file.fileId"
      @click="downloadFile_(file.fileId)"
    >
      {{ file.originalName }}
    </p>
    <span>댓글</span>
    <div
      v-if="commentList != null"
      v-for="comment in commentList"
      :key="comment.commentId"
    >
      <p>{{ parseStringFormat(comment.createdAt, 'YYYY-MM-DD HH:mm') }}</p>
      <p>{{ comment.comment }}</p>
    </div>
    <form @submit.prevent="createComment_">
      <input type="text" v-model="comment" />
      <button type="submit">등록</button>
    </form>
  </div>
  <button @click="goToList">목록</button>
  <button @click="goToModify">수정</button>
  <button @click="deleteButton">삭제</button>

  <div v-show="showDeleteButton">
    <label>비밀번호</label>
    <input type="password" v-model="password" />
    <button @click="deleteBoard_">입력</button>
    <button @click="deleteButton">취소</button>
  </div>
</template>
<script>
import { useRoute, useRouter } from 'vue-router';
import { ref } from 'vue';
import { parseStringFormat } from '@/pages/utils/stringUtils';
import { getBoard, checkPassword, deleteBoard } from '@/api/boardService';
import { getFileList, downloadFile } from '@/api/fileService';
import { getCommentList, createComment } from '@/api/commentService';

export default {
  setup() {
    const route = useRoute();
    const router = useRouter();
    const boardId = route.params.id;
    // 검색 조건 쿼리 데이터
    const startDate = route.query.startDate;
    const endDate = route.query.endDate;
    const categoryId = route.query.categoryId;
    const searchText = route.query.searchText;
    const pageNum = route.query.pageNum;
    // 페이지 사용될 변수들
    // TODO : Object 설정? -> 초기값
    const board = ref(null);
    const commentList = ref([]);
    const fileList = ref([]);
    // inputData
    const comment = ref('');
    const password = ref('');
    // show button
    const showDeleteButton = ref(false);

    /**
     * 게시물 GET요청
     * @returns {Promise<void>}
     */
    const getBoard_ = async () => {
      try {
        const res = await getBoard(boardId, 'view');
        board.value = res;
      } catch (error) {
        goToError();
      }
    };
    getBoard_();

    /**
     * 파일 리스트 GET 요청
     * @returns {Promise<void>}
     */
    const getFileList_ = async () => {
      const res = await getFileList(boardId);
      fileList.value = res;
    };
    getFileList_();

    /**
     * 댓글들 GET 요청
     * @returns {Promise<void>}
     */
    const getCommentList_ = async () => {
      const res = await getCommentList(boardId);
      commentList.value = res;
    };
    getCommentList_();

    /**
     * 댓글 POST 요청
     * @returns {Promise<void>}
     */
    const createComment_ = async () => {
      await createComment(boardId, comment.value);
      // 댓글 등록 후 댓글 리스트 가져오기
      await getCommentList_();
      // 댓글 입력칸 초기화
      comment.value = '';
    };

    /**
     * 게시물 DELETE 요청
     * @returns {Promise<void>}
     */
    const deleteBoard_ = async () => {

      const res = await checkPassword(boardId, password.value);
      if (res.data.status == 400) {
        alert('password incorrect');
        return;
      }
      await deleteBoard(boardId);
      // 삭제 후 목록 페이지 이동
      goToList();
    };

    /**
     * 파일 다운로드 GET 요청
     * @param fileId
     * @returns {Promise<void>}
     */
    const downloadFile_ = async (fileId) => {
      const res = await downloadFile(fileId);
      let fileName = '';

      for (const file of fileList.value) {
        if (file.fileId == fileId) {
          fileName = file.originalName;
        }
      }
      // download object 설정
      const url = window.URL.createObjectURL(new Blob([res]));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', `${fileName}`);
      document.body.appendChild(link);
      link.click();
      link.remove();
    };
    /**
     * 비밀번호 확인 창
     */
    const deleteButton = () => {
      showDeleteButton.value = !showDeleteButton.value;
    };

    const goToList = () => {
      router.push({
        name: 'List',
        query: {
          startDate: startDate,
          endDate: endDate,
          categoryId: categoryId,
          searchText: searchText,
          pageNum: pageNum,
        },
      });
    };

    const goToModify = () => {
      router.push({
        name: 'Modify',
        query: {
          boardId: boardId,
          startDate: startDate,
          endDate: endDate,
          categoryId: categoryId,
          searchText: searchText,
          pageNum: pageNum,
        },
      });
    };

    const goToError = () => {
      router.push({
        name: 'Error',
      });
    };

    return {
      board,
      commentList,
      fileList,
      comment,
      password,
      showDeleteButton,
      goToList,
      parseStringFormat,
      createComment_,
      deleteBoard_,
      goToModify,
      deleteButton,
      downloadFile_,
    };
  },
};
</script>
<style></style>
