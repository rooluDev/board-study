# 📋 MVC

## 📝 프로젝트 개요
이 프로젝트는 게시판을 MVC 아키텍처(Spring Boot & Thymeleaf)를 활용해 구축하는 것을 목표로 합니다.

게시판의 주 기능인 글 작성, 수정, 삭제 등 을 구현했습니다.

## 💡 주요 기능
+ 게시판 작성
  <details>
   <summary>코드 보기(펼치기/접기)</summary>
  
    텍스트 입력과 파일 첨부가 모두 가능한 게시판 작성 기능을 구현하였습니다.이를 위해 multipart/form-data 형식을 사용하여 클라이언트에서 전송된 게시글 정보(제목, 내용, 작성자 등)와 첨부파일을 함께 서버로 전달하고,서버에서는 이를 파싱하여 각각 DB에 저장하거나 파일로 저장하는 로직을 구성하였습니다.
  
    Controller
     ```
    /**
     * 게시판 등록 POST
     *
     * @param board    등록할 게시물
     * @param fileList 등록할 파일들
     * @return list
     */
    @PostMapping(value = {"/board/post"})
    public String postBoard(@ModelAttribute BoardDTO board, @RequestParam(name = "file",required = false) List<MultipartFile> fileList) throws IOException {
        // 유효성 검사
        if (!BoardValidator.validateBoardForPost(board)) {
            return "redirect:/error";
        }


        // board 저장
        Long boardId = boardService.postBoard(board);

        // file 저장
        if (fileList != null && !fileList.isEmpty()) {
            fileService.uploadFile(fileList, board.getBoardId());
        }

        return "redirect:/board/list";
    }
     ```
  fileService.uplodFile
    ```
    /**
     * File Upload
     *
     * @param fileList 저장할 파일 리스트
     * @param boardId  board PK
     */
    public void uploadFile(List<MultipartFile> fileList, Long boardId) throws IOException {
        for (MultipartFile multipartFile : fileList) {
            if (!multipartFile.isEmpty()) {
                // File DTO 생성
                FileDTO file = FileDTO.builder()
                        .boardId(boardId)
                        .originalName(multipartFile.getOriginalFilename())
                        .physicalName(UUID.randomUUID().toString())
                        .filePath(REAL_PATH)
                        .extension(MultipartFileUtils.extractExtension(multipartFile.getOriginalFilename()))
                        .size(multipartFile.getSize())
                        .build();

                // Server 저장
                String filePath = REAL_PATH + file.getPhysicalName() + "." + file.getExtension();
                File uploadedFile = new File(filePath);
                FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), uploadedFile);

                // File DB Add
                fileMapper.insertFile(file);
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
     * @param board            수정할 게시물
     * @param fileList         등록할 파일
     * @param deleteFileIdList 삭제할 파일 pk 리스트
     * @return list
     */
    @PostMapping(value = {"/board/edit/{boardId}"})
    public String updateBoard(@ModelAttribute BoardDTO board,
                              @PathVariable Long boardId,
                              @RequestParam(name = "newFile", required = false) List<MultipartFile> fileList,
                              @RequestParam(name = "deleteFileIdList", required = false) List<Long> deleteFileIdList) throws IOException {

        if (!BoardValidator.validateBoardForEdit(board)) {
            return "redirect:/board/list";
        }
        // 게시물 수정
        board.setBoardId(boardId);
        boardService.editBoard(board);

        // 파일 삭제
        if (deleteFileIdList != null && !deleteFileIdList.isEmpty()) {
            deleteFileIdList.forEach(fileService::deleteById);
        }

        if (fileList != null && !fileList.isEmpty()) {
            fileService.uploadFile(fileList, board.getBoardId());
        }

        return "redirect:/board/list";
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
     * @return redirect /board/list
     */
    @GetMapping(value = {"/board/delete/{boardId}"})
    public String deleteBoard(@PathVariable Long boardId) {
        // 삭제
        commentService.deleteCommentListByBoardId(boardId);
        fileService.deleteFileListByBoardId(boardId);
        boardService.deleteBoard(boardId);

        return "redirect:/board/list";
    }
    
     ```
     
  
+ 게시판 검색
  <details>
   <summary>코드 보기(펼치기/접기)</summary>
    
    검색 조건을 SearchCondition 클래스를 별도로 두고, 기본 생성자에서 기본값을 설정하여 사용자의 입력이 없더라도 기본 검색 조건이 자동 적용되도록 구성하였습니다. 또한 getStartDateTimestamp(), getEndDateTimestamp(), getStartRow() 메서드들을 커스터마이징하여, 컨트롤러나 서비스 레이어에서 불필요한 계산 로직 없이 직관적으로 사용할 수 있게 하여 코드의 간결성과 재사용성을 높였습니다.
    
    Controller
  
     ```
    /**
     * 게시판 - 목록 페이지
     *
     * @param model           Model
     * @param searchCondition 검색조건
     * @return list
     */
    @GetMapping(value = {"/board/list"})
    public String getBoardList(Model model, @ModelAttribute SearchCondition searchCondition) {

        // 필요한 정보들 가져오기
        List<BoardDTO> boardList = boardService.getBoardListByCondition(searchCondition);
        int boardCount = boardService.getBoardCountByCondition(searchCondition);
        List<CategoryDTO> categoryList = categoryService.getCategoryList();

        // 필요한 정보들 설정
        int totalPageNum = (int) Math.ceil((double) boardCount / (double) searchCondition.getPageSize());

        // 정보들 넘겨주기
        model.addAttribute("boardList", boardList);
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("totalPageNum", totalPageNum);
        model.addAttribute("boardCount", boardCount);
        model.addAttribute("searchCondition", searchCondition);

        return "list";
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
+ 비밀번호 확인 프로세스
  <details>
   <summary>코드 보기(펼치기/접기)</summary>

    
  게시글 수정 및 삭제 시 공통적으로 필요한 비밀번호 검증 로직을 하나의 Controller에서 처리하여 중복을 제거하였습니다. 프론트엔드에서는 AJAX를 통해 서버에 비밀번호를 비동기 요청으로 전송하고, 응답 결과에 따라 수정 또는 삭제 동작을 분기 처리합니다.
  이를 통해 사용자 경험을 저해하지 않으면서도, 하나의 엔드포인트로 재사용 가능한 보안 로직을 구현하고 유지보수성을 높였습니다.
    
  View.html
  ```
    $(document).ready(function () {
            $("#confirmPasswordBtn").click(function () {
                const enteredPassword = $("#passwordInput").val();
                // AJAX 요청
                $.ajax({
                    type: "POST",
                    url: "/board/passwordConfirm",
                    data: {
                        boardId: boardId,
                        enteredPassword: enteredPassword
                    },
                    success: function () {
                        if (actionType === "edit") {
                            goToEdit();
                        } else if (actionType === "delete") {
                            deleteBoard();
                        }
                    },
                    error: function () {
                        alert("비밀번호 불일치");
                    }
                });
            });
        });

     ```
    Controller
    ```
    /**
     * 비밀번호 확인
     *
     * @param boardId         pk
     * @param enteredPassword 입력한 비밀번호
     * @return ResponseEntity
     */
    @PostMapping(value = {"/board/passwordConfirm"})
    public ResponseEntity confirmPassword(@RequestParam(name = "boardId") Long boardId, @RequestParam(name = "enteredPassword") String enteredPassword) {

        // 비밀번호 불일치
        if (!boardService.findByIdAndPassword(boardId, enteredPassword)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        //비밀번호 일치
        return ResponseEntity.ok().build();
    }
    ```

## 🛠 기술 스택
![Spring Boot](https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white)
![MyBatis](https://img.shields.io/badge/MyBatis-555555?style=for-the-badge&logo=MyBatis&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-00618A?style=for-the-badge&logo=mysql&logoColor=white)
