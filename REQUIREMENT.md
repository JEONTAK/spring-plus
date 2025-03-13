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

#### 위치 : [TodoController](src/main/java/org/example/expert/domain/todo/controller/TodoController.java), [TodoService](src/main/java/org/example/expert/domain/todo/service/TodoService.java), [TodoRepository](src/main/java/org/example/expert/domain/todo/repository/TodoRepository.java)

- 테스트에서 원하는 것은 400 BAD_REQUEST가 발생하는 것을 원함.
- 따라서 status().isOk()부분을 status().is4xxClientError()로 변경함.
- 또한 value값을 OK가 아닌 BAD_REQUEST로 변경함.

---

## Lv1-4요구사항 - (전탁 작성 2025.03.12)

### 컨트롤러 테스트의 이해

- 테스트 패키지 org.example.expert.domain.todo.controller의 todo_단건_조회_시_todo가_존재하지_않아_예외가_발생한다() 테스트가 실패하고 있으므로, 수정하여 정상 동작하도록 해야 한다.

### 해결

#### 위치 : [TodoControllerTest](src/test/java/org/example/expert/domain/todo/controller/TodoControllerTest.java)

- TodoController에 RequestParam으로 weather, start, end 값을 추가함.
- 해당 값들은 required = false로 입력되지 않으면 null로 처리 됨.
- TodoService의 getTodos에서 start와 end에 대한 값을 LocalDateTime으로 변환하고 todoRepository에 weather, startDay, endDay 값을 파라미터로 사용하여 요청
- TodoRepository에서 JPQL 쿼리를 새로 생성하여 findAllByCondition이라는 이름을 통해 반환.

---

## Lv1-5요구사항 - (전탁 작성 2025.03.12)

### AOP의 이해

- AOP는 UserAdminController 클래스의 changeUserRole() 메서드 실행 전 동작해야 한다.
- AdminAccessLoggingAspect 클래스에 있는 AOP를 수정하여 위 요구사항을 충족해야 한다.

### 해결

#### 위치 : [AdminAccessLoggingAspect](src/main/java/org/example/expert/aop/AdminAccessLoggingAspect.java)

- AdminAccessLoggingAspect 부분에 @After 를 @Before로 바꿔 메서드 실행전 동작할 수 있도록 수정.
- UserController의 getUser 메서드가 아닌, UserAdminController의 changeUserRole 메서드가 실행될 때 동작할 수 있도록 수정.

---

## Lv2-6요구사항 - (전탁 작성 2025.03.12)

### JPA Cascade

- 할 일을 새로 저장할 시, 할 일을 생성한 유저는 담당자로 자동 등록 되어야 한다.
- JPA의 Cascade 기능을 활용해 할 일을 생성한 유저가 담당자로 등록될 수 있게 하여야 한다.

### 해결

#### 위치 : [Todo](src/main/java/org/example/expert/domain/todo/entity/Todo.java)

- managers에서 기존 @OneToMany(mappedBy = "todo") 를 @OneToMany(mappedBy = "todo", cascade = CascadeType.ALL)로 수정하여 manager까지 함께 영속화 시키도록 수정함.

---

## Lv2-7요구사항 - (전탁 작성 2025.03.12)

### N + 1

- CommentController 클래스의 getComments() API를 호출할 때 N + 1 문제가 발생하고 있으므로 수정해야 한다.

### 해결

#### 위치 : [CommentRepository](src/main/java/org/example/expert/domain/comment/repository/CommentRepository.java)

- 기존 findByTodoIdWithUser에서는 유저의 정보를 같이 가져오지 않았기 때문에, FETCH를 추가하여 유저의 정보들도 같이 가져올 수 있도록 수정함.

---

## Lv2-8요구사항 - (전탁 작성 2025.03.12)

### QueryDSL

- JPQL로 작성된 findByIdWithUser(TodoService)를 QueryDSL로 변경해야 한다.
- N + 1 문제가 발생해서는 안된다.

### 해결

#### 위치 : [TodoRepositoryCustom](src/main/java/org/example/expert/domain/todo/repository/TodoRepositoryCustom.java), [TodoRepositoryCustomImpl](src/main/java/org/example/expert/domain/todo/repository/TodoRepositoryCustomImpl.java)

- QueryDSL을 사용하기 위해 gradle에 의존성을 추가.
- QueryDSL을 사용하기위 해 TodoRepositoryCustom 인터페이스 추가 생성하여 TodoRepository에 implements 함
- TodoRepositoryCustom Interface를 정의한 TodoRepositoryCustomImpl class 생성하여 findByIdWithUser 메서드 작성
- findByIdWithUser는 todoId에 맞는 할 일과 해당 유저의 값을 반환.
- 따라서 Q객체로 todo와 user를 생성한 후, queryFactory를 통해 query 생성
  - 쿼리는 다음과 같음.
    - selectFrom을 통해 todo로 부터 값을 찾겠다 선언
    - leftJoin을 통해 user 정보를 join하여 찾겠다 선언. 이때 fetchJoin을 통해 값을 가져올 수 있도록 설정
    - where문을 통해 todo.id가 todoId와 같은 todo를 찾음
    - fetchOne()을 통해 단일 객체를 가져옴.
