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
    <div v-if="fileList != null">
      <p
        v-for="file in fileList"
        :key="file.fileId"
        @click="downloadFile(file.fileId)"
      >
        {{ file.originalName }}
      </p>
    </div>
    <span>댓글</span>
    <div v-if="commentList != null">
      <div v-for="comment in commentList" :key="comment.commentId">
        <p>{{ parseStringFormat(comment.createdAt, 'YYYY-MM-DD HH:mm') }}</p>
        <p>{{ comment.comment }}</p>
      </div>
    </div>
    <form @submit.prevent="addComment">
      <input type="text" v-model="comment" />
      <button type="submit">등록</button>
    </form>
  </div>
  <button @click="goToList">목록</button>
  <button @click="showCheckPasswordForm('edit')">수정</button>
  <button @click="showCheckPasswordForm('delete')">삭제</button>

  <div v-show="deleteButton">
    <label>비밀번호</label>
    <input type="password" v-model="password" />
    <button @click="checkPassword">입력</button>
    <button @click="cancel">취소</button>
    <span>{{ unauthorized }}</span>
  </div>
</template>
<script>
import { useRoute, useRouter } from 'vue-router';
import { onMounted, ref } from 'vue';
import { parseStringFormat } from '@/pages/utils/utils';
import {
  fetchGetBoard,
  fetchCheckPassword,
  fetchDeleteBoard,
  fetchAddView,
} from '@/api/boardService';
import { fetchGetFileResource } from '@/api/fileService';
import { fetchAddComment, fetchGetCommentList } from '@/api/commentService';

export default {
  setup() {
    const route = useRoute();
    const router = useRouter();
    const boardId = route.params.id;
    // 페이지 사용될 변수들
    const board = ref({});
    const commentList = ref([]);
    const fileList = ref([]);
    // inputData
    const comment = ref('');
    const password = ref('');
    // show button
    const deleteButton = ref(false);
    const option = ref('');
    const unauthorized = ref('');

    onMounted(async () => {
      await getBoard();
      await addView();
    });

    /**
     * 게시물 GET 요청
     *
     * @returns {Promise<void>}
     */
    const getBoard = async () => {
      const res = await fetchGetBoard(boardId);
      board.value = res.board;
      fileList.value = res.fileList;
      commentList.value = res.commentList;
    };

    const addView = async () => {
      await fetchAddView(boardId);
    };

    /**
     * 댓글 POST 요청
     * @returns {Promise<void>}
     */
    const addComment = async () => {
      await fetchAddComment(boardId, comment.value);
      // 댓글 등록 후 댓글 리스트 가져오기
      commentList.value = await fetchGetCommentList(boardId);
      // 댓글 입력칸 초기화
      comment.value = '';
    };

    /**
     * 파일 다운로드 GET 요청
     * @param fileId
     * @returns {Promise<void>}
     */
    const downloadFile = async (fileId) => {
      const res = await fetchGetFileResource(fileId);
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
     * 게시물 DELETE 요청
     * @returns {Promise<void>}
     */
    const deleteBoard = async () => {
      await fetchDeleteBoard(boardId);
      // 삭제 후 목록 페이지 이동
      goToList();
    };

    /**
     * 비밀번호 확인
     * @returns {Promise<void>}
     */
    const checkPassword = async () => {
      const formData = new FormData();
      formData.append('boardId', boardId);
      formData.append('enteredPassword', password.value);
      try {
        await fetchCheckPassword(formData);
      } catch (e) {
        unauthorized.value = '비밀번호가 일치하지 않습니다.';
        return;
      }
      if (option.value === 'edit') {
        goToEdit();
      } else if (option.value === 'delete') {
        await deleteBoard();
      }
    };

    /**
     * 비밀번호 확인 창 보여주기
     */
    const showCheckPasswordForm = (option_) => {
      option.value = option_;
      deleteButton.value = !deleteButton.value;
    };

    /**
     * 비밀번호 확인 창 지우기
     */
    const cancel = () => {
      deleteButton.value = false;
    };

    const goToList = () => {
      router.push({
        name: 'List',
        query: route.query,
      });
    };

    const goToEdit = () => {
      router.push({
        name: 'Edit',
        params: {
          id: boardId,
        },
        query: route.query,
      });
    };

    return {
      board,
      commentList,
      fileList,
      comment,
      password,
      deleteButton,
      unauthorized,
      showCheckPasswordForm,
      checkPassword,
      goToList,
      addComment,
      deleteBoard,
      parseStringFormat,
      downloadFile,
      goToEdit,
      cancel,
    };
  },
};
</script>
<style></style>
