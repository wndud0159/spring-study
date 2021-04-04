## 프로그래밍에서 의존성이란?
- 스프링의 제어의역전(Inversion Of Control)라고도 하는 DI(Dependency Injection)을 알아보기 전에 의존성이 무엇인지 알아보자
```
- 의사 코드
운전자가 자동차를 생산한다.
자동차는 내부적으로 타이어를 생산한다.

- 자바로 표현
new Car();
Car 객체 생성자에서 new Tire();

- 의존성을 단순하게 정의하면 다음과 같다
의존성은 new다.
new를 실행하는 Car와 Tire사이에서 Car가 Tire에 의존한다.

"더 깊게 들어가면 의존하는객체(전체)와 의존되는객체(부분) 사이에 집합관계(Aggregation)와 구성관계(Composition)로 구분할 수도 있다."
```
- 집합관계
  -  부분이 전체와 다른 생명주기를 가질 수 있다.(예:집 vs 냉장고)
- 구성관계
  - 부분은 전체와 같은 생명주기를 갖는다.(예:사람 vs 심장)
---
---
## 스프링 없이 의존성 주입하기1 - 생성자를 통한 의존성 주입
```java
class Car {
    Tire tire;

    Car(Tire tire) {
        this.tire = tire;
    }
}
interface Tire {
    //내용 생략
}
class KoreaTire implements Tire {
    //내용 생략
}
class AmericaTire implements Tire {
    //내용 생략
}

public class Driver {
    public static void main(String[] args) {
        Tire tire = new KoreanTire();
        Car car = new Car(tire);
    }
}

/*
- 의사코드
운전자가 타이어를 생산한다.
운전자가 자동차를 생산하면서 타이어를 장착한다

주입이란?
주입이란 말은 외부에서라는 뜻을 내포하고 있는 단어다.
결국 자동차 내부에서 타이어를 생산하는 것이 아니라 외부에서 생상된 타이어를 자동차에 장착하는 주입이다.

전략 패턴의 3요소
전략 : Tire를 구현한 KoreaTire,AmericaTire
컨텍스트 : Car의 getTireBrand()메서드 / 나는 안만듬;;
클라이언트 : Driver의 main()메서드
*/
``` 
## 스프링 없이 의존성 주입하기2 - 속성을 통한 의존성 주입
```java
class Car {
    Tire tire;

    Car() {};

    setTire(Tire tire) {
        this.tire = tire;
    }
}
interface Tire {
    //내용 생략
}
class KoreaTire implements Tire {
    //내용 생략
}
class AmericaTire implements Tire {
    //내용 생략
}

public class Driver {
    public static void main(String[] args) {
        Tire tire = new KoreanTire();
        Car car = new Car();
        //속성 접근자 메서드 사용 저자는 이렇게 써놨는데.. 설정자메서드가 아닌지... 싶네..
        car.setTire(tire);
    }
}

/*
- 의사코드
운전자가 타이어를 생산한다.
운전자가 자동차를 생산한다.
운전자가 자동차에 타이어를 장착한다.
*/
``` 
- `최근에는 속성을 통한 의존성주입보다는 생성자를 통한 의존성 주입을 선호하는 사람들이 많다. 프로그램에서는 한번 주입된 의존성을 계속 사용하는 경우가 더 일반적이기 때문`
---
---
## 스프링을 통한 의존성 주입 - XML 파일 사용
```xml
<!-- ex01.xml -->
<beans>
<bean id="koreaTire" class="ex01.KoreaTire"></bean>
<bean id="americaTire" class="ex01.AmericaTire"></bean>
<bean id="car" class="ex01.Car"></bean>
</beans>
```
```java
class Car {
    Tire tire;

    Car() {};

    setTire(Tire tire) {
        this.tire = tire;
    }
}
interface Tire {
    //내용 생략
}
class KoreaTire implements Tire {
    //내용 생략
}
class AmericaTire implements Tire {
    //내용 생략
}

public class Driver {
    public static void main(Stirng[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("ex01.xml");

        Car car = context.getBean("car", Car.class);
        Tire tire = context.getBean("koreaTire", Tire.class);

        car.setTire(tire); // KoreaTire 장착
    }
}

/*
- 의사코드
운전자가 종합 쇼핑몰에서 타이어를 구매한다.
운전자가 종합 쇼핑몰에서 자동차를 구매한다.
운전자가 자동차에 타이어를 장착한다.
*/
```
## 스프링을 통한 의존성주입 - 스프링 설정 파일(XML)에서 속성 주입
```xml
<!-- ex01.xml -->
<beans>
<bean id="koreaTire" class="ex01.KoreaTire"></bean>
<bean id="americaTire" class="ex01.AmericaTire"></bean>
<bean id="car" class="ex01.Car">
    <property name="tire" ref="koreaTire"></property>
    <!-- Car클래스 setTire메서드를 xml파일의 property태그를 이용해 대체하는 것-->
</bean>
</beans>
```
```java
class Car {
    Tire tire;

    Car() {};

    setTire(Tire tire) {
        this.tire = tire;
    }
}
interface Tire {
    //내용 생략
}
class KoreaTire implements Tire {
    //내용 생략
}
class AmericaTire implements Tire {
    //내용 생략
}

public class Driver {
    public static void main(Stirng[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("ex01.xml");

        Car car = context.getBean("car", Car.class);
    }
}

/*
- 의사 코드 - 점점 더 현실 세계를 닮아간다.
운전자가 종합 쇼핑몰에서 자동차를 구매 요청한다.
종합 쇼필몰은 자동차를 생산한다.
종합 쇼핑몰은 타이어를 생산한다.
종합 쇼핑몰은 자동차에 타이어를 장착한다.
종합 쇼핑몰은 운전자에게 자동차를 전달한다.
*/
```
## 스프링을 통한 의존성 주입 - @Autowired를 통한 속성 주입
```xml
<!-- ex01.xml -->
<beans>

<context:anotation-config />


<bean id="tire" class="ex01.KoreaTire"></bean>
<bean id="americaTire" class="ex01.AmericaTire"></bean>
<bean id="car" class="ex01.Car">
</bean>
</beans>
```
```java
class Car {
    // 오토와이드 어노테이션을 이용하면 설정자 메서드를 이용하지 않고 스프링 프레임워크가 설정파일을 통해 설정자 메서드 대신 속성을 주입해준다 사용하기 전 xml파일 NameSpaces탭에서 context 체크
    @Autowired
    Tire tire;

    Car() {};

    setTire(Tire tire) {
        this.tire = tire;
    }
}
interface Tire {
    //내용 생략
}
class KoreaTire implements Tire {
    //내용 생략
}
class AmericaTire implements Tire {
    //내용 생략
}

public class Driver {
    public static void main(Stirng[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("ex01.xml");

        Car car = context.getBean("car", Car.class);
    }
}
/*
- 오토와이트어노테이션 "tire" 네임과 xml 빈설정파일 id="tire" 가 일치하지 않아도 Tire 타입 구현 객체가 빈에 하나만(두개면 오류)이라도 등록 되어있다면 자동으로 주입 해준다(Type우선)
*/
```
## 스프링을 이용한 의존성 주입 - @Resource를 통한 속성 주입
- @Autowired와 @Resource 어노테이션은 두 객체 사이에 의존성을 해결해 준다. 둘을 비교해 보겠다.

 흠냐 | @Autowired | @Resource
 ---|---|---
 출처 | 스프링 프레임워크 | 표준 자바
 소속 패키지 | org.springframework.beans.factory.anotation.Autowired | javax.anotation.Resource
 빈 검색 방식(우선순위) | byType 먼저, 못찾으면 byName | byName먼저, 못찾으면 byType
 특이사항 | @Qalifier("")협업 | name 어트리뷰트
 byName 강제하기 | @Autowired쓰고 @Qualifier("tire") | @Resource(name="tire")

```java
 class Car {
    // 리소스 어노테이션은 자바기반의 어노테이션이다 Autowired 말고 리소스를 추천하는 이유는 스프링이 아닌 다른 프레임워크로 교체되는 경우를 대비하면 @Resource가 더 유리하다
    @Reosource
    Tire tire;

    Car() {};

    setTire(Tire tire) {
        this.tire = tire;
    }
}
```
- 저자의 말로는 Autowired보단 @Resource, @Resource보단 property태그 라고 한다