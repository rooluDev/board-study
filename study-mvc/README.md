# 📋 MVC

## 📝 프로젝트 개요

이 프로젝트는 **Spring Boot & Thymeleaf 기반 MVC 아키텍처**를 활용해 게시판을 구현한 학습용 프로젝트입니다.  
게시판의 주요 기능인 **글 작성, 수정, 삭제, 검색, 비밀번호 검증, 파일 업로드** 등을 구현하며,  
Spring MVC 패턴을 활용한 계층형 아키텍처 설계와 서버-뷰 간 데이터 바인딩을 학습하는 것을 목표로 했습니다.

## 💡 주요 기능

### 1️⃣ 게시판 작성

텍스트 입력과 파일 첨부가 모두 가능한 게시판 작성 기능을 구현했습니다. `multipart/form-data` 형식을 사용하여 게시글 정보와 첨부 파일을 동시에 전송하고,  
서버에서 이를 파싱해 **DB 저장 + 로컬 파일 저장** 로직을 구성했습니다.

  <details>
   <summary>코드 보기(펼치기/접기)</summary>  
  
    Controller
     
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
     
  fileService.uplodFile
    
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

     
  </details>

### 2️⃣ 게시판 수정

  작성자가 비밀번호를 입력해 본인 글을 수정할 수 있는 기능입니다.
새로운 파일 추가 및 기존 파일 삭제가 가능하며, 모든 변경사항은 DB에 반영됩니다.

  <details>
   <summary>코드 보기(펼치기/접기)</summary>
  
    Controller
     
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
    
  </details>
       
### 3️⃣ 게시판 삭제

게시글과 첨부 파일, 댓글을 한 번에 삭제하는 기능입니다.
비밀번호 확인 후 모든 관련 데이터를 안전하게 제거합니다.

  <details>
   <summary>코드 보기(펼치기/접기)</summary>
  
    Controller
     
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
    
  </details>
     
  
### 4️⃣ 게시판 검색

검색 조건을 SearchCondition 클래스에서 관리하여
날짜, 카테고리, 검색어, 페이징 처리를 효율적으로 수행할 수 있도록 구현했습니다.
컨트롤러와 서비스 레이어에서 불필요한 로직 반복을 줄였습니다.

  <details>
   <summary>코드 보기(펼치기/접기)</summary>
    
    Controller
  
    
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

### 5️⃣ 비밀번호 확인 프로세스

게시글 수정 및 삭제 시 공통적으로 필요한 비밀번호 검증 로직을
AJAX 기반 비동기 요청으로 처리해 UX를 개선하고, 하나의 엔드포인트로 로직을 재사용했습니다.

  <details>
   <summary>코드 보기(펼치기/접기)</summary>

    
  View.html
  
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

     
    Controller
    
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
  </details>

## 🛠 기술 스택
![Spring Boot](https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white)
![MyBatis](https://img.shields.io/badge/MyBatis-555555?style=for-the-badge&logo=MyBatis&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-00618A?style=for-the-badge&logo=mysql&logoColor=white)
