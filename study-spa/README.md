# 📋 SPA (Single Page Application)

## 📝 프로젝트 개요
이 프로젝트는 **Spring Boot (백엔드)**와 **Vue.js (프론트엔드)**를 활용하여 SPA 아키텍처 기반의 게시판을 구현한 학습용 프로젝트입니다.  
게시판의 주요 기능인 **글 작성, 수정, 삭제, 검색, 예외 처리, 검색조건 유지**를 구현하며,  
REST API를 활용한 클라이언트-서버 간 데이터 통신 및 상태 관리 방식을 학습하는 것을 목표로 했습니다.

## 💡 주요 기능

### 1️⃣ 게시판 작성

텍스트 입력과 파일 첨부가 모두 가능한 게시판 작성 기능을 구현했습니다.  
`multipart/form-data` 형식을 사용하여 게시글과 파일을 동시에 서버에 전송하고,  
서버는 이를 파싱해 **DB 저장 + 파일 업로드**를 처리합니다.
  
  <details>
   <summary>코드 보기(펼치기/접기)</summary>

    Controller
  
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

    FileService.addFile()
    
    
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
  </details>

### 2️⃣ 게시판 수정
  
RESTful 규칙에 맞게 PUT 요청을 활용하여 게시글 수정 기능을 구현했습니다.
파일 추가 및 기존 파일 삭제를 동시에 처리할 수 있으며, 데이터는 트랜잭션 단위로 업데이트됩니다.

  <details>
   <summary>코드 보기(펼치기/접기)</summary>

    Controller
  
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
  </details>

  
### 3️⃣ 게시판 삭제

댓글, 첨부파일, 게시글을 한 번에 삭제하는 기능입니다.
REST API의 DELETE 요청을 사용하여 클라이언트에서 간결하게 호출할 수 있도록 구현했습니다.

  <details>
   <summary>코드 보기(펼치기/접기)</summary>
    
    Controller
  
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

  </details>
    
### 4️⃣ 게시판 검색

검색 조건을 객체(SearchCondition)로 관리하여 날짜, 카테고리, 검색어, 페이징 처리를 효율적으로 수행할 수 있도록 구현했습니다.
기본 생성자에서 기본값을 설정하여, 사용자가 검색 조건을 입력하지 않아도 자동으로 적용됩니다.

  <details>
   <summary>코드 보기(펼치기/접기)</summary>

    Controller
  
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

    SearchCondition
    
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
  </details>  

### 5️⃣ 예외처리

애플리케이션 전반에서 발생할 수 있는 예외를 일관되게 처리하기 위해
@RestControllerAdvice를 활용한 전역 예외 처리 로직을 구현했습니다.

  <details>
   <summary>코드 보기(펼치기/접기)</summary>
  
    GlobalExceptionHandler
  
     
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

  </details>

  
### 6️⃣ 검색조건 유지

게시글 작성/조회 후 목록 페이지로 돌아가도 이전 검색 조건을 유지할 수 있도록
Vue Router의 query 기능을 활용해 UX를 개선했습니다.

  <details>
   <summary>코드 보기(펼치기/접기)</summary>

    ListBoard.vue
  
     
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

     

  ViewBoard.vue

    
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
</details>


## 🛠 기술 스택
![Thymeleaf](https://img.shields.io/badge/thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white)
![Spring Boot](https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Vue.js](https://img.shields.io/badge/vue.js-4FC08D?style=for-the-badge&logo=vue.js&logoColor=white)
![MyBatis](https://img.shields.io/badge/MyBatis-000000?style=for-the-badge&logo=MyBatis&logoColor=white)
