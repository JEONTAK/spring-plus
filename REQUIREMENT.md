# 요구사항 정리 📝

## 문서의 목적

- 이 문서에서는 **[ 플러스 주차 개인 과제 ]** 의 요구 사항을 정의합니다.

---

## 문서 기본 정보

- **작성자** : 전탁
- **작성일** : 2025.03.13 (목)
- **수정일** : 2025.03.13 (목)

---

## Lv1-1요구사항 - (전탁 작성 2025.03.12)

### @Transactional의 이해

- 할 일 저장 기능을 구현한 API('/todos')를 호출할 때, 에러가 발생하고 있으므로, 수정하여야 한다.

### 해결

#### 위치 : [TodoService](src/main/java/org/example/expert/domain/todo/service/TodoService.java)

- TodoService 전체가 @Transactional(readOnly = true)로 설정되어 있어 Database와의 연결이 읽기로만 가능함.
- 따라서 saveTodos 메서드에 @Transactional을 붙여 INSERT 쿼리가 정상적으로 실행할 수 있도록 수정.

---

## Lv1-2요구사항 - (전탁 작성 2025.03.12)

### JWT의 이해

- User의 정보에 nickname이 필요하므로, User 테이블에 nickname 컬럼을 추가하여야 한다.
- nickname은 중복가능하여야 한다.
- JWT에는 기존 데이터에 추가로 nickname도 들어가야 한다.

### 해결

#### 위치 : [User](src/main/java/org/example/expert/domain/user/entity/User.java), [SignupRequest](src/main/java/org/example/expert/domain/auth/dto/request/SignupRequest.java), [JwtUtil](src/main/java/org/example/expert/config/JwtUtil.java), [AuthService](src/main/java/org/example/expert/domain/auth/service/AuthService.java)

- User Entity에 nickname 컬럼을 추가함.
- SignupRequest에 nickname 컬럼을 추가하여 회원가입 시 nickname 저장할 수 있도록 수정.
- JwtUtil에서 createToken시 nickname도 토큰에 넣어 생성할 수 있도록 수정.
- AuthService에서 토큰 반환 시 nickname도 토큰에 넣어 반환할 수 있도록 수정.

---

## Lv1-3요구사항 - (전탁 작성 2025.03.12)

### JPA의 이해

- 할 일 검색 시 weather 조건으로도 검색할 수 있어야 한다.
    - weather 조건은 있을 수도 있고, 없을 수도 있다.
- 할 일 검색 시 수정일 기준으로 기간 검색이 가능해야 한다.
    - 기간의 시작과 끝 조건은 있을 수도 있고, 없을 수도 있다.
- JPQL을 사용하고, 쿼리 메소드 명은 자유롭게 지정해도 된다.

### 해결

#### 위치 : [TodoControllerTest](src/test/java/org/example/expert/domain/todo/controller/TodoControllerTest.java)

- 테스트에서 원하는 것은 400 BAD_REQUEST가 발생하는 것을 원함.
- 따라서 status().isOk()부분을 status().is4xxClientError()로 변경함.
- 또한 value값을 OK가 아닌 BAD_REQUEST로 변경함.

---

## Lv1-4요구사항 - (전탁 작성 2025.03.12)

### 컨트롤러 테스트의 이해

- 테스트 패키지 org.example.expert.domain.todo.controller의 todo_단건_조회_시_todo가_존재하지_않아_예외가_발생한다() 테스트가 실패하고 있으므로, 수정하여 정상 동작하도록 해야 한다.

### 해결

#### 위치 : [TodoController](src/main/java/org/example/expert/domain/todo/controller/TodoController.java), [TodoService](src/main/java/org/example/expert/domain/todo/service/TodoService.java), [TodoRepository](src/main/java/org/example/expert/domain/todo/repository/TodoRepository.java)

- TodoController에 RequestParam으로 weather, start, end 값을 추가함.
- 해당 값들은 required = false로 입력되지 않으면 null로 처리 됨.
- TodoService의 getTodos에서 start와 end에 대한 값을 LocalDateTime으로 변환하고 todoRepository에 weather, startDay, endDay 값을 파라미터로 사용하여 요청
- TodoRepository에서 JPQL 쿼리를 새로 생성하여 findAllByCondition이라는 이름을 통해 반환.