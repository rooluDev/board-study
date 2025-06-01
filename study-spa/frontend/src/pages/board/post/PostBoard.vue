<template>
  <h1>게시판 - 등록</h1>
  <form @submit.prevent="postBoard">
    <label>카테고리</label>
    <select v-model="boardFormData.categoryId">
      <option
        v-for="category in categoryList"
        :value="category.categoryId"
        :key="category.categoryId"
      >
        {{ category.categoryName }}
      </option>
    </select>
    <br />
    <label>작성자</label>
    <input
      type="text"
      v-model="boardFormData.userName"
      minlength="3"
      maxlength="4"
      required
    />
    <br />
    <label>비밀번호</label>
    <input
      type="password"
      v-model="boardFormData.password"
      minlength="4"
      maxlength="15"
      required
    />
    <input
      type="password"
      v-model="boardFormData.passwordRe"
      minlength="4"
      maxlength="15"
      required
    />
    <br />
    <label>제목</label>
    <input
      type="text"
      v-model="boardFormData.title"
      minlength="4"
      maxlength="99"
      required
    />
    <br />
    <label>내용</label>
    <input
      type="text"
      v-model="boardFormData.content"
      minlength="4"
      maxlength="1999"
      required
    />
    <br />
    <div v-for="index in 3" :key="index" style="display: inline">
      <input type="file" @change="addFile($event, index)" />
    </div>
    <br />
    <button type="submit">저장</button>
    <button @click="goToList('back')">취소</button>
  </form>
</template>
<script>
import { onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { fetchGetCategoryList } from '@/api/categoryService';
import { fetchPostBoard } from '@/api/boardService';

export default {
  setup() {
    // param 설정
    const router = useRouter();
    const route = useRoute();

    // 페이지 구성에 필요한 데이터
    const categoryList = ref([
      { categoryId: -1, categoryName: '카테고리 선택' },
    ]);

    // form data
    const boardFormData = ref({
      categoryId: -1,
      userName: '',
      password: '',
      passwordRe: '',
      title: '',
      content: '',
      fileList: [],
    });

    onMounted(async () => {
      await getCategoryList();
    });

    /**
     * input 카테고리 리스트
     * @returns {Promise<void>}
     */
    const getCategoryList = async () => {
      const res = await fetchGetCategoryList();
      res.forEach((category) => {
        categoryList.value.push(category);
      });
    };

    /**
     * 파일 등록시 파일 ListPush
     * @param event
     */
    const addFile = (event, index) => {
      const file = event.target.files[0];
      boardFormData.value.fileList[index] = file;
    };

    /**
     * 파일 클릭시 파일 다운로드
     * @returns {Promise<void>}
     */
    const postBoard = async () => {
      // 비밀번호 확인
      if (!checkPasswordRegex() || !checkPasswordEqual()) {
        alert('password incorrect');
        return;
      }

      const formData = new FormData();
      formData.append('categoryId', boardFormData.value.categoryId);
      formData.append('userName', boardFormData.value.userName);
      formData.append('title', boardFormData.value.title);
      formData.append('content', boardFormData.value.content);
      formData.append('password', boardFormData.value.password);
      formData.append('passwordRe', boardFormData.value.passwordRe);
      boardFormData.value.fileList.forEach((file) => {
        if (file != null) {
          formData.append('file', file);
        }
      });

      await fetchPostBoard(formData);

      await router.push({
        name: 'List',
      });
    };

    /**
     * 비밀번호, 비밀번호 확인 일치 여부
     * @returns {boolean}
     */
    const checkPasswordEqual = () => {
      return boardFormData.value.password === boardFormData.value.passwordRe;
    };

    /**
     * 비밀번호 regex 확인
     * @returns {boolean}
     */
    const checkPasswordRegex = () => {
      const regex = /^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*?_]).{4,15}$/;
      return regex.test(boardFormData.value.password);
    };

    /**
     * 리스트 페이지로
     */
    const goToList = () => {
      router.push({
        name: 'List',
        query: route.query,
      });
    };

    return {
      categoryList,
      boardFormData,
      addFile,
      postBoard,
      goToList,
    };
  },
};
</script>
<style></style>
