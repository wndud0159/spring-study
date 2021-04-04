## AOP - Aspect? 관점? 핵심관심사? 횐단 관심사?
- 스프링의 3대 프로그래밍 모델 중 두 번째는 AOP다
- Aspect Oriented Programming 이를 번역하면 관점지향프로그래밍 이 된다
- 스프링 DI가 의존성(new)에 대한 주입이라면 AOP는 로직(code)주입이라고 할 수 있다.
```java
/*
- 프로그램을 작성하다 보면 이처럼 다수의 모듈에 공통적으로 나타나는 부분이 존재하는데, 바로 이것을 횡단관심사(cross-cutting concern)이라고 한다.
- 데이터베이스 연동 프로그램을 작성한 적이 있다면 insert든 연산이든,update,delete,selete 연산이든 항상 반복해서 등장하는 다음과 같은 형태의 코드가 있다
*/
DB 커넥션 준비
Statement 객체 준비

try {
    DB커넥션 연결
    statement 객체 세팅
    // insert / update / delete / selete 실행
} catch... {
    예외처리
} finaly {
    DB자원 반납
}

/*
- 주석처리 부분 뺴고는 공통적으로 나타나는 코드다.
- 이를 바로 횡단관심사 라고 한다.
- 그리고 주석이 있는 insert...부분을 핵심 관심사 라고 한다

"코드 = 핵심관심사 + 횡단관심사"
*/
```
## AOP 용어
용어 | 영한사전 
---|---
Aspect | 관점, 측면, 양상
Advisor | 조언자, 고문
Advice | 조언, 충고
JoinPoint | 결합점
Pointcut | 자르는 점
- 위 5가지 용어와 스프링 AOP의 숨어있는 프록시를 이해하면 스프링 AOP활용하는 데 충분하다.
## Pointcut - Aspect 적용 위치 지정자!
- Pointcut이라고 하는 것은 횡단관심사를 적용할 타깃 메서드를 선택하는 지시자(메서드 선택필터)
  - `"타깃 클래스의 타깃 메서드 지정자"`
- 스프링 AOP만 보자면 Aspect를 메서드에만 적용할 수 있으니 타깃메서드지정자 라는 말이 틀리지 않다. 하지만 다른 프레임워크에서는 메서드 뿐만 아니라 속성 등에도 Aspect를 적용할 수 있기에 그것들까지 고려하면 Aspect적용취지지정자(지시자)가 맞는 표한 같다네요
```
타깃 메서드 지정자에는 익히 알려진 정규식과 AspectJ표현식 등을 사용할 수 있다. 밑

[접근제한자패턴] 리턴타입패턴 [패키지&클래스패턴]메서드이름패턴(파라미터패턴)[throws예외패턴]

여기서 대괄호[]는 생략이 가능하다는 의미
```
## JoinPoint - 연결 가능한 지점
- Pointcut은 JoinPoint의 부분 집합이다
- Aspect 적용이 가능한 모든 지점
- 광의의 JoinPoint란 Aspect 적용이 가능한 모든 지점이다.
- 협의의 JoinPoint란 호출된 객체의 메서드다.
## Advice - 언제, 무엇을
- Pointcut에 언제, 무엇을 적용할지 정의한 메서드

Adivce | 수행지점
---|---
before | 포인트컷(타킷 메서드) 전에 수행
after | 포인트컷(타깃 메서드) 후에 수행(타깃메서드가 예외일어나든 정상수행되든 수행)
after returning | 포인트컷(타깃 메서드) 정상수행 후에 수행
after throwing | 포인트컷(타깃 메서드) 예외발생 후에 수행
around | 포인트컷(타깃 메서드) 전 후 에 수행
## Advisor - 어디서,언제,무엇을
- Advisor는 스프링 AOP에서만 사용하는 용어이며 다른 AOP 프레임워크에서는 사용하지 않는다
- 이제는 쓰지 말라고 권고하는 기능이기도 하다.
- 다수의 Advice와 다수의 Pointcut을 다양하게 조합해서 사용하는 Aspect가 나왔기 때문
- `Advisor = 하나의Pointcut + 하나의Advice`
## Aspect - Advisor의 집합체
- AOP에서 Aspect는 여러 개의 Advice와 여러 개의 Pointcut의 결합체를 의미하는 용어다
- `Aspect = Advice들 + Pointcut들`
---
---
## XML을 활용한 AOP 구현
```xml
<!-- ex/ex01.xml -->
<beans>

<aop:aspectJ-autoproxy />

<bean id="aopTest" class="ex.XmlAopTest"/>
<bean id="boy" class="ex.Boy"/>

<aop:config>
    <aop:aspect ref="aopTest">
        <aop:pointcut id="xmlPointCut" expression="execution(public void xe.Boy.runSomething())"/>
        <aop:around method="aroundAOP" pointcut-ref="xmlPointCut"/>
        <aop:before method="breforeAOP" pointcut-ref="xmlPointCut"/>
        <aop:after method="afterAOP" pointcut-ref="xmlPointCut"/>
        <aop:after-returning method="afterReturningAOP" pointcut-ref="xmlPointCut" returning="retValue"/>
        <aop:after-throwing method="afterThrowing" pointcut-ref="xmlPointCut" throwing="ex"/>


</beans>
```
```java
// ex/Boy
public class Boy implements Person {
    @Override
    public coid runSomething() {
        System.out.println("컴퓨터로 게임을 한다");
    }
}


// ex/XmlAopTest
public class XmlAopTest {
 
  public void breforeAOP(JoinPoint joinPoint){
    System.out.println("----------------------XML-----------breforeAOP");
   
    Class clazz = joinPoint.getTarget().getClass();
    String className = joinPoint.getTarget().getClass().getSimpleName();
    String methodName = joinPoint.getSignature().getName();
    String classAndMethod = joinPoint.getSignature().toShortString();
        
    System.out.println("#########clazz######========" + joinPoint.getTarget().getClass());
        System.out.println("########joinPoint.getTarget()#######========" + joinPoint.getTarget());
        System.out.println("########className#######========" + className);
        System.out.println("#########methodName######========" + methodName);
        System.out.println("##########joinPoint.toLongString()#####========" + joinPoint.toLongString());
        System.out.println("##########joinPoint.toShortString()#####========" + joinPoint.toShortString());
        System.out.println("##########classAndMethod#####========" + classAndMethod);
  }
 
public Object aroundAOP(ProceedingJoinPoint joinPoint)  throws Throwable{
  System.out.println("----------------------XML----함수진입------aroundAOP");
  long start = System.nanoTime();
    try {
      System.out.println("----------------------XML----해당 메소드 실행 전------aroundAOP");
     Object result = joinPoint.proceed();// 대상객체의 메서드 실행(ProceedingJoinPoint 타입은 대상 객체의 메서드를 호출할 때 사용)
     return result;
    } finally {
      System.out.println("----------------------XML----해당 메소드 실행 후------aroundAOP");
     long finish = System.nanoTime(); //1/1000000000
     Signature sig = joinPoint.getSignature(); //메서드의 시그니쳐
     System.out.printf("%s.%s(%s) 실행 시간 : %d ns\n",
       joinPoint.getTarget().getClass().getSimpleName(),
       sig.getName(), Arrays.toString(joinPoint.getArgs()),
       (finish - start)/1000000000);
      
       System.out.println("##############joinPoint.toLongString()===================" + joinPoint.toLongString()); //대상 메서드 전체 syntax 리턴
       //execution(public abstract javax.sql.DataSource com.ibatis.sqlmap.client.SqlMapTransactionManager.getDataSource())
        
       System.out.println("##############joinPoint.toShortString()===================" + joinPoint.toShortString()); // 대상 메소드명 리턴
       //execution(SqlMapTransactionManager.getDataSource())
        
       System.out.println("##############joinPoint.getTarget()===================" + joinPoint.getTarget()); //대상객체를 리턴
       //com.ibatis.sqlmap.engine.impl.SqlMapClientImpl@1dc7b3e
        
       System.out.println("##############joinPoint.getSignature()===================" + joinPoint.getSignature()); //호출되는 메소드 정보
       //DataSource com.ibatis.sqlmap.client.SqlMapTransactionManager.getDataSource()
        
       System.out.println("##############joinPoint.getTarget().getClass().getSimpleName()===================" + joinPoint.getTarget().getClass().getSimpleName());
       //SqlMapClientImpl
    }
    
}
 
public void afterAOP(){
  System.out.println("----------------------XML----------afterAOP");
}
 
//JoinPoint 는 대상 target 객체 정보를 보유한 결합지점
  //retValue 는 target 메서드가 실행한 후 return 값
public void afterReturningAOP(JoinPoint joinPoint, Object retValue){
     System.out.println("---------------XML--------------afterReturningAOP");
    String targetClass=joinPoint.getTarget().getClass().getName();
    String methodName=joinPoint.getSignature().getName();
     
    System.out.println("########logging!! className="+targetClass+"  methodName="+methodName +" retVal : "+retValue);
   
    }
 
  public void afterThrowing(Throwable ex){
    System.out.println(ex.getMessage() + " :############## @AfterThrowing");
    }
}
```
---
---
---
---
## 어노테이션 활용 AOP 구현
```xml
<!-- ex/ex01.xml -->
<beans>

<aop:aspectJ-autoproxy />

<bean id="aopTest" class="ex.AnotationAopTest"/>
<bean id="boy" class="ex.Boy"/>

</beans>
```
```java
// ex/Boy
public class Boy implements Person {
    @Override
    public coid runSomething() {
        System.out.println("컴퓨터로 게임을 한다");
    }
}

// ex/AnotationAoptest
@Aspect
public class AnnotationAopTest {
 
  @Pointcut("execution(* runSomething())")
    public void annotationAOP() {
     // 여긴 무엇을 작성해도 의미가 없어요..
    }
 
   @Before("annotationAOP()")
   public void before(JoinPoint joinPoint){
     System.out.println("----------------------Annotation-----------beforeAOP");
   }
   @After("annotationAOP()")
   public void after(){
     System.out.println("----------------------Annotation-----------afterAOP");
   }
  @AfterReturning(pointcut="annotationAOP()",  returning="retValue")
  public void afterReturningAOP(JoinPoint joinPoint, Object retValue){
     System.out.println("---------------Annotation--------------afterReturningAOP");
   
   }
  @Around("annotationAOP()")
    public Object measure(ProceedingJoinPoint joinPoint) throws Throwable {
       long start = System.nanoTime(); //현재의 나노시간을 반환
        try {
          System.out.println("---------------Annotation--------------AroundAOP");
         Object result = joinPoint.proceed(); // 대상객체의 메서드 실행(ProceedingJoinPoint 타입은 대상 객체의 메서드를 호출할 때 사용)
         return result;
        } finally {
          System.out.println("---------------Annotation--------------AroundAOP");
         long finish = System.nanoTime();
         Signature sig = joinPoint.getSignature(); //메서드의 시그니쳐
         System.out.printf("%s.%s(%s) 실행 시간 : %d ns\n",
           joinPoint.getTarget().getClass().getSimpleName(),
           sig.getName(),
           Arrays.toString(joinPoint.getArgs()), //인자목록을 반환
           (finish - start));
        }
    }
    
  @AfterThrowing(pointcut="annotationAOP()", throwing="ex") //예외값 지정
    public void after_throwing(Throwable ex){
    System.out.println("---------------Annotation--------------AfterThrowing");
    }
      
}
```