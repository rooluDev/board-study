<template>
  <h1>게시판 - 등록</h1>
  <form @submit.prevent="uploadBoardForm">
    <label>카테고리</label>
    <select v-model="boardFormData.selectedCategoryId">
      <option value="-1">카테고리 선택</option>
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
      v-model="boardFormData.passwordCheck"
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
    <input type="file" @change="fileInput" />
    <input type="file" @change="fileInput" />
    <input type="file" @change="fileInput" />
    <br />
    <button type="submit">저장</button>
    <button @click="goToList('back')">취소</button>
  </form>
</template>
<script>
import { ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { getCategoryList } from '@/api/categoryService';
import { uploadBoard } from '@/api/boardService';

export default {
  setup() {
    // param 설정
    const router = useRouter();
    const route = useRoute();
    const startDate = route.query.startDate;
    const endDate = route.query.endDate;
    const category = route.query.categoryId;
    const searchText = route.query.searchText;
    const pageNum = route.query.pageNum;
    // 페이지 구성에 필요한 데이터
    const categoryList = ref([]);
    // form data
    const boardFormData = ref({
      selectedCategoryId: '',
      userName: '',
      password: '',
      passwordCheck: '',
      title: '',
      content: '',
      files: [],
    });

    /**
     * input 카테고리 리스트
     * @returns {Promise<void>}
     */
    const getCategories = async () => {
      const res = await getCategoryList();
      categoryList.value = res;
    };
    getCategories();

    /**
     * 파일 등록시 파일 ListPush
     * @param event
     */
    const fileInput = (event) => {
      boardFormData.value.files = boardFormData.value.files.concat(
        Array.from(event.target.files),
      );
    };

    /**
     * 파일 클릭시 파일 다운로드
     * @returns {Promise<void>}
     */
    const uploadBoardForm = async () => {
      // 비밀번호 확인
      if (!checkPasswordRegex() || !checkPasswordEqual()) {
        alert('password incorrect');
      }

      // 파일 저장 POST
      await uploadBoard(boardFormData.value);
      goToList('upload');
    };

    /**
     * 비밀번호, 비밀번호 확인 일치 여부
     * @returns {boolean}
     */
    const checkPasswordEqual = () => {
      return boardFormData.value.password === boardFormData.value.passwordCheck;
    };

    /**
     * 비밀번호 regex 확인
     * @returns {boolean}
     */
    const checkPasswordRegex = () => {
      const regex = /^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*?_]).{4,15}$/;
      return regex.test(boardFormData.value.password);
    };

    const goToList = (option = 'back') => {
      if (option == 'back') {
        router.push({
          name: 'List',
          query: {
            startDate: startDate,
            endDate: endDate,
            categoryId: category,
            searchText: searchText,
            pageNum: pageNum,
          },
        });
      } else if (option == 'upload') {
        router.push({
          name: 'List',
        });
      }
    };

    return {
      categoryList,
      boardFormData,
      fileInput,
      uploadBoardForm,
      goToList,
    };
  },
};
</script>
<style></style>
