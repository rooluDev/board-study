# 📋 Medel1

## 📝 프로젝트 개요
이 프로젝트는 게시판 포털 사이트를 구축하는 것을 목표로 합니다. 

게시판의 종류로는 자유 게시판, 문의 게시판, 갤러리 게시판과 공지사항 총 4개의 게시판으로 구성되어 있습니다.

서버를 2개를 두어 사용자 페이지와 관리자 페이지를 나누어 제작했습니다.

사용자 페이지는 SPA(Single Page Application)로 SpringBoot와 Vue.js를 통해 제작했으며. 반면, 관리자 페이지는 MPA(Multi Page Application)로 SpringBoot와 Thymeleaf를 통해 제작했습니다.

## 💡 주요 기능
+ 자유게시판 작성
  <details>
   <summary>코드 보기(펼치기/접기)</summary>
  
    Controller
     ```
      @PostMapping("/board/free")
        public ResponseEntity addBoard(@Valid @ModelAttribute FreeBoardDto freeBoardDto,
                                       @RequestPart(name = "file", required = false) MultipartFile[] fileList,
                                       HttpServletRequest request) {
    
            ...
    
            return ResponseEntity.ok().build();
    
     ```
     [Controller 전체코드](https://github.com/rooluDev/board-portal-project/blob/main/user-page/backend/src/main/java/com/user/backend/controller/FreeBoardController.java#L132-L171)
