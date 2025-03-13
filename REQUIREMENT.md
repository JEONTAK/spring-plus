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
