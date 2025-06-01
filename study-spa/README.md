# 📋 SPA (Single Page Application)

## 📝 프로젝트 개요
이 프로젝트는 게시판을 SPA(Spring Boot & Vue.js)를 활용해 구축하는 것을 목표로 합니다.

게시판의 주 기능인 글 작성, 수정, 삭제 등 을 구현했습니다.

## 💡 주요 기능
+ 게시판 작성
  <details>
   <summary>코드 보기(펼치기/접기)</summary>

    텍스트 입력과 파일 첨부가 모두 가능한 게시판 작성 기능을 구현하였습니다.이를 위해 multipart/form-data 형식을 사용하여 클라이언트에서 전송된 게시글 정보(제목, 내용, 작성자 등)와 첨부파일을 함께 서버로 전달하고,서버에서는 이를 파싱하여 각각 DB에 저장하거나 파일로 저장하는 로직을 구성하였습니다.

    Controller
  
    ```
    /**
     * 게시물 추가
     *
     * @param boardDto 추가할 게시물 데이터
     * @param fileList 추가할 파일 리스트
     * @return null
     */
    @PostMapping("/board")
    public ResponseEntity postBoard(@ModelAttribute BoardDto boardDto,
                                    @RequestPart(name = "file", required = false) List<MultipartFile> fileList) throws IOException {


        // 유효성 검사
        if (!BoardValidator.validateBoardForPost(boardDto)) {
            throw new IllegalStateException();
        }

        // DB에 board 저장
        Long boardId = boardService.postBoard(boardDto);

        // File 저장
        if (fileList != null && !fileList.isEmpty()) {
            fileService.addFile(fileList, boardId);
        }

        return ResponseEntity.ok().body(null);
    }
    ```

    FileService.addFile()
    
    ```
    /**
     * File Upload
     *
     * @param multipartFiles 추가할 파일
     * @param boardId 게시판 pk
     */
    public void addFile(List<MultipartFile> multipartFiles, Long boardId) throws IOException {
        if(multipartFiles != null) {
            for (MultipartFile multipartFile : multipartFiles) {
                if (!multipartFile.isEmpty()) {
                    // File DTO 생성
                    FileDto file = FileDto.builder()
                            .boardId(boardId)
                            .originalName(multipartFile.getOriginalFilename())
                            .physicalName(UUID.randomUUID().toString())
                            .filePath(path)
                            .extension(MultipartFileUtils.extractExtension(multipartFile.getOriginalFilename()))
                            .size(multipartFile.getSize())
                            .build();

                    // Server 저장
                    saveFileRepository.createFile(file,multipartFile);

                    // File DB Add
                    fileMapper.insertFile(file);
                }
            }
        }
    }
    ```

+ 게시판 수정
  <details>
   <summary>코드 보기(펼치기/접기)</summary>

    Controller
  
     ```
    /**
     * 게시물 수정
     *
     * @param boardId          pk
     * @param boardDto         수정할 게시물 데이터
     * @param fileList         추가할 파일
     * @param deleteFileIdList 삭제할 파일의 pk 리스트
     * @return null
     */
    @PutMapping("/board/{boardId}")
    public ResponseEntity updateBoard(@PathVariable(name = "boardId") Long boardId,
                                      @ModelAttribute BoardDto boardDto,
                                      @RequestPart(name = "file", required = false) List<MultipartFile> fileList,
                                      @RequestPart(name = "deleteFileIdList", required = false) List<Long> deleteFileIdList) throws IOException {

        if (!BoardValidator.validateBoardForEdit(boardDto)) {
            throw new IllegalStateException();
        }
        boardDto.setBoardId(boardId);
        boardService.editBoard(boardDto);

        if (deleteFileIdList != null && !deleteFileIdList.isEmpty()) {
            deleteFileIdList.forEach(fileService::deleteById);
        }

        if (fileList != null && !fileList.isEmpty()) {
            fileService.addFile(fileList, boardId);
        }

        return ResponseEntity.ok().body(null);
    }
     ```
+ 게시판 삭제
  <details>
   <summary>코드 보기(펼치기/접기)</summary>
    
    Controller
  
     ```
    /**
     * 게시물 삭제
     *
     * @param boardId pathVariable
     * @return null
     */
    @DeleteMapping("/board/{boardId}")
    public ResponseEntity deleteBoard(@PathVariable Long boardId) {
        commentService.deleteCommentListByBoardId(boardId);
        fileService.deleteFileListByBoardId(boardId);
        boardService.deleteBoard(boardId);

        return ResponseEntity.ok().body(null);
    }
     ```
    
+ 게시판 검색
  <details>
   <summary>코드 보기(펼치기/접기)</summary>
    
    검색 조건을 SearchCondition 클래스를 별도로 두고, 기본 생성자에서 기본값을 설정하여 사용자의 입력이 없더라도 기본 검색 조건이 자동 적용되도록 구성하였습니다. 또한 getStartDateTimestamp(), getEndDateTimestamp(), getStartRow() 메서드들을 커스터마이징하여, 컨트롤러나 서비스 레이어에서 불필요한 계산 로직 없이 직관적으로 사용할 수 있게 하여 코드의 간결성과 재사용성을 높였습니다.


    Controller
  
     ```
    /**
     * 게시판 리스트 페이지에 필요한 데이터
     *
     * @param searchCondition 검색조건
     * @return {
     * boardList : []
     * totalPageNum : 0
     * }
     */
    @GetMapping("/boards")
    public ResponseEntity<Map<String, Object>> getBoardList(@ModelAttribute SearchCondition searchCondition) {

        List<BoardDto> boardList = boardService.getBoardListByCondition(searchCondition);
        int boardCount = boardService.getBoardCountByCondition(searchCondition);

        Map<String, Object> response = new HashMap<>();
        response.put("boardList", boardList);
        response.put("boardCount", boardCount);

        return ResponseEntity.ok().body(response);
    }
    ```

     
    SearchCondition
    ```
    /**
     * 검색 조건
     */
    @Getter
    @Setter
    public class SearchCondition {
        private String startDate;
        private String endDate;
        private int categoryId;
        private String searchText;
        private int pageNum;
        private int startRow;
        private int pageSize = 10;
        private Timestamp startDateTimestamp;
        private Timestamp endDateTimestamp;
    
        /**
         * 기본 검색 조건 생성자
         */
        public SearchCondition() {
            this.startDate = LocalDate.now().minusYears(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            this.endDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            this.categoryId = -1;
            this.searchText = "";
            this.pageNum = 1;
        }
    
        public Timestamp getStartDateTimestamp() {
            LocalDate localStateDate = LocalDate.parse(this.startDate);
            LocalDateTime startDate = localStateDate.atTime(LocalTime.MIN);
            return Timestamp.valueOf(startDate);
        }
    
        public Timestamp getEndDateTimestamp() {
            LocalDate localEndDate = LocalDate.parse(this.endDate);
            LocalDateTime endDate = localEndDate.atTime(LocalTime.MAX);
            return Timestamp.valueOf(endDate);
        }
    
        /**
         * OFFSET Getter
         *
         * @return offset
         */
        public int getStartRow() {
            if (pageNum == 1) {
                return 0;
            }
            return (this.pageNum - 1) * this.pageSize;
        }
    
    }
    ```  

+ 예외처리
  <details>
   <summary>코드 보기(펼치기/접기)</summary>

    애플리케이션 전반에서 발생할 수 있는 예외를 일관되게 처리하기 위해, @RestControllerAdvice를 활용한 GlobalExceptionHandler를 구현하였습니다.
  
    GlobalExceptionHandler
  
     ```
    /**
     * RestController에서 발생하는 Exception 처리하는 GlobalExceptionHandler
     */
    @Slf4j
    @RestControllerAdvice
    public class GlobalExceptionHandler {
    
        /**
         * IllegalStateException 처리 Handler
         *
         * @return BAD_REQUEST BAD_REQUEST
         */
        protected ResponseEntity handleIllegalStateException(IllegalStateException e){
            log.error("IllegalStateException occurred. message={}", e.getMessage(), e);
    
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

     ...

     ```
+ 검색조건 유지
  <details>
   <summary>코드 보기(펼치기/접기)</summary>

    ListBoard.vue
  
     ```
    // 검색 조건
    const searchCondition = ref({
      startDate:
        route.query.startDate ||
        moment().subtract(1, 'year').format('YYYY-MM-DD'),
      endDate: route.query.endDate || moment().format('YYYY-MM-DD'),
      categoryId: route.query.category || -1,
      searchText: route.query.searchText || '',
      pageNum: route.query.pageNum || 1,
      pageSize: 10,
    });
     
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
          startDate: searchCondition.value.startDate,
          endDate: searchCondition.value.endDate,
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
        name: 'Post',
        query: {
          startDate: searchCondition.value.startDate,
          endDate: searchCondition.value.endDate,
          categoryId: searchCondition.value.categoryId,
          searchText: searchCondition.value.searchText,
          pageNum: searchCondition.value.pageNum,
        },
      });
    };

     ```

  ViewBoard.vue

    ```
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
    ```


## 🛠 기술 스택
![Thymeleaf](https://img.shields.io/badge/thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white)
![Spring Boot](https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Vue.js](https://img.shields.io/badge/vue.js-4FC08D?style=for-the-badge&logo=vue.js&logoColor=white)
![MyBatis](https://img.shields.io/badge/MyBatis-000000?style=for-the-badge&logo=MyBatis&logoColor=white)
