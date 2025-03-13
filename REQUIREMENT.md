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
