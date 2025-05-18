<template>
  <h1>게시판 - 수정</h1>
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
  <form @submit.prevent="editBoard">
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
    <div v-for="(file, index) in fileList" :key="index">
      <span>{{ file.originalName }}</span>
      <button type="button" @click="removeFile(file.fileId, index)">X</button>
    </div>
    <div v-for="(file, index) in addedFileList" :key="index">
      <input type="file" @change="fileChange(index, $event)" />
      <span v-if="file">{{ file.name }}</span>
      <button type="button" @click="removeNewFile(index)">X</button>
    </div>
    <button type="button" @click="addFile">첨부파일 추가</button>
    <br />
    <button type="submit">저장</button>
    <button type="button" @click="goToView">취소</button>
  </form>
</template>
<script>
import { useRouter, useRoute } from 'vue-router';
import { computed, onMounted, ref } from 'vue';
import { parseStringFormat } from '@/pages/utils/utils';
import { fetchEditBoard, fetchGetBoard } from '@/api/boardService';

export default {
  setup() {
    const router = useRouter();
    const route = useRoute();
    // 검색조건 저장
    const boardId = route.params.id;
    // 페이지에 쓰는 데이터 설정
    const board = ref({});
    const fileList = ref([]);
    // input data
    const password = ref('');
    const deletedFilesList = ref([]);
    const addedFileList = ref([]);

    const maxFileSize = 3;
    const existingFileSize = computed(() => fileList.value.length);
    const newFileSize = computed(() => addedFileList.value.length);

    onMounted(async () => {
      await getBoard();
    });

    /**
     * 게시물 데이터 GET
     * @returns {Promise<void>}
     */
    const getBoard = async () => {
      const res = await fetchGetBoard(boardId);
      board.value = res.board;
      fileList.value = res.fileList;
    };

    /**
     * 게시물 수정 PUT
     * @returns {Promise<void>}
     */
    const editBoard = async () => {
      const formData = new FormData();
      formData.append('userName', board.value.userName);
      formData.append('title', board.value.title);
      formData.append('content', board.value.content);
      addedFileList.value.forEach((file) => {
        formData.append('file', file);
      });
      deletedFilesList.value.forEach((deletedFileId) => {
        formData.append('deletedFileId', deletedFileId);
      });

      await fetchEditBoard(boardId, formData);

      // 게시판 - 보기 페이지 이동
      goToView();
    };

    const removeFile = (fileId, index) => {
      deletedFilesList.value.push(fileId);
      fileList.value.splice(index, 1);
    };

    const addFile = () => {
      if (existingFileSize.value + newFileSize.value >= maxFileSize) {
        return alert(`첨부파일은 최대 ${maxFileSize}개까지 가능합니다.`);
      }
      addedFileList.value.push(null);
    };

    const fileChange = (index, event) => {
      const file = event.target.files[0];
      if (file == null) {
        return;
      }
      addedFileList.value[index] = file;
    };

    const removeNewFile = (index) => {
      addedFileList.value.splice(index, 1);
    };

    const goToView = () => {
      router.push({
        name: 'View',
        params: {
          id: boardId,
        },
        query: route.query,
      });
    };

    return {
      board,
      fileList,
      password,
      addedFileList,
      goToView,
      parseStringFormat,
      editBoard,
      removeFile,
      addFile,
      fileChange,
      removeNewFile,
    };
  },
};
</script>
<style></style>
