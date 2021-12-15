drop table worker purge;

-- 관리자정보 저장 테이블
create table worker(          
    id          varchar2(20)  primary key,
    pwd         varchar2(20),
    name        varchar2(40),
    phone       varchar2(20)
);

-- 기본키를 삭제하되 참조되는 항목들도 같이 삭제하세요
alter table member drop primary key cascade;

drop table member purge;

create table member(   
    id         varchar2(20)  primary key,
    pwd        varchar2(20),     
    name       varchar2(40),
    email      varchar2(40),
    zip_num    varchar2(7),    -- 우편번호
    address    varchar2(100),
    phone      varchar2(20),
    useyn      char(1)       default 'y',    --현재 id 사용여부(활동중/비활동중)
    indate     date          default sysdate     --가입일
);



alter table product drop primary key cascade;
drop table product purge;
create table product(
    pseq       number(5)     primary key,
    name       varchar2(100),
    kind       char(1),   
    price1     number(7)     default '0',     --원가
    price2     number(7)     default '0',     --판매가
    price3     number(7)     default '0',     --수익
    content    varchar2(1000),                 --상품설명 내용
    image      varchar2(50)  default 'default.jpg',    --상품이미지
    useyn      char(1)       default 'y',    -- 현재 판매중여부
    bestyn     char(1)       default 'n',    --베스트 상품 y/n
    indate     date          default sysdate  --등록일
);

drop sequence product_seq;

create sequence product_seq start with 1;





alter table cart drop primary key cascade;
drop table cart purge;

create table cart (
  cseq         number(10)    primary key,  -- 장바구니번호
  id  varchar2(16)   references member(id),   -- 주문자 아이디(FK :　member.id) 
  pseq  number(5)     references product(pseq),  -- 주문 상품번호(FK :product.pseq) 
  quantity     number(5)     default 1,        -- 주문 수량
  result       char(1)       default '1',      -- 1:미처리 2:처리
  indate       date          default SYSDATE   -- 주문일
);

drop sequence cart_seq;
create sequence cart_seq start with 1;




alter table orders drop primary key cascade;
drop table orders purge;

create table orders(
  oseq        number(10)    primary key,           -- 주문번호  
  id          varchar2(16)   references member(id), -- 주문자 아이디
  indate      date          default sysdate       -- 주문일
);

drop sequence orders_seq;
create sequence orders_seq start with 1;



alter table order_detail drop primary key cascade;
drop table order_detail purge;

create table order_detail(
  odseq       number(10)   primary key,        -- 주문상세번호
  oseq        number(10)   references orders(oseq),   -- 주문번호  
  pseq        number(5)    references product(pseq),  -- 상품번호
  quantity    number(5)    default 1,                 -- 주문수량
  result      char(1)      default '1'                -- 1: 미처리 2: 처리     
);
drop sequence order_detail_seq;
create sequence order_detail_seq start with 1;

select * from order_detail





alter table address drop primary key cascade;
drop table address purge;

CREATE TABLE address (
       zip_num              VARCHAR2(7) NOT NULL,
       sido                 VARCHAR2(30) NOT NULL,
       gugun                VARCHAR2(30) NOT NULL,
       dong                 VARCHAR2(100) NOT NULL,
       zip_code             VARCHAR2(30) ,
       bunji                VARCHAR2(30) 
);






alter table qna drop primary key cascade;
drop table qna purge;

create table qna (
  qseq        number(5)    primary key,  -- 글번호 
  subject     varchar2(300),            -- 제목
  content     varchar2(1000),          -- 문의내용
  reply       varchar2(1000),           -- 답변내용
  id          varchar2(20),                 -- 작성자(FK : member.id) 
  rep         char(1)       default '1',        --1:답변 무  2:답변 유  
  indate      date default  sysdate     -- 작성일
); 

drop sequence qna_seq;
create sequence qna_seq start with 1;



insert into worker values('admin', 'admin', '관리자', '010-7777-7777');
insert into worker values('scott', 'tiger', '강희준', '010-6400-6068');


insert into member(id, pwd, name, zip_num, address, phone) values
('one', '1111', '김나리', '133-110', '서울시 성동구 성수동1가 1번지21호', 
'017-777-7777');
insert into member(id, pwd, name, zip_num, address, phone)values
('two', '2222', '김길동', '130-120', 
'서울시 송파구 잠실2동 리센츠 아파트 201동 505호', '011-123-4567');



insert into product(pseq, name, kind, price1, price2, price3, content, image) 
values(product_seq.nextval, '크로그다일부츠', '2', 40000, 50000, 10000, 
'오리지랄 크로그다일부츠 입니다.', 'w2.jpg');
insert into product(pseq, name, kind, price1, price2, price3, content, image, 
bestyn) values(product_seq.nextval, '롱부츠', '2', 40000, 50000, 10000,
'따뜻한 롱부츠 입니다.', 'w-28.jpg', 'n');
insert into product(pseq,  name, kind, price1, price2, price3, content, image, 
bestyn) values(product_seq.nextval, '힐', '1', 10000, 12000, 2000, 
'여성용전용 힐', 'w-26.jpg', 'n');
insert into product(pseq,  name, kind, price1, price2, price3, content, image,
bestyn)values(product_seq.nextval, '슬리퍼', '4', 5000, 5500, 500, 
'편안한 슬리퍼입니다.', 'w-25.jpg', 'y');
insert into product(pseq,  name, kind, price1, price2, price3, content, image,
bestyn)values(product_seq.nextval, '회색힐', '1', 10000, 12000, 2000, 
'여성용전용 힐', 'w9.jpg', 'n');
insert into product(pseq,  name, kind, price1, price2, price3, content, image) 
values(product_seq.nextval, '여성부츠', '2', 12000, 18000, 6000, 
'여성용 부츠', 'w4.jpg');
insert into product(pseq,  name, kind, price1, price2, price3, content, image, 
bestyn)values(product_seq.nextval,  '핑크샌달', '3', 5000, 5500, 500,
'사계절용 샌달입니다.', 'w-10.jpg', 'y');
insert into product(pseq,  name, kind, price1, price2, price3, content, image, 
bestyn)values(product_seq.nextval, '슬리퍼', '3', 5000, 5500, 500, 
'편안한 슬리퍼입니다.', 'w11.jpg', 'y');
insert into product(pseq,  name, kind, price1, price2, price3, content, image) 
values( product_seq.nextval,  '스니커즈', '4', 15000, 20000, 5000,
'활동성이 좋은 스니커즈입니다.', 'w1.jpg');
insert into product(pseq,  name, kind, price1, price2, price3, content, image, 
bestyn)values( product_seq.nextval,  '샌달', '3', 5000, 5500, 500, 
'사계절용 샌달입니다.', 'w-09.jpg','n');
insert into product(pseq,  name, kind, price1, price2, price3, content, image,
bestyn)values( product_seq.nextval,  '스니커즈', '5', 15000, 20000, 5000, 
'활동성이 좋은 스니커즈입니다.', 'w-05.jpg','n');


insert into cart(cseq,id, pseq, quantity) values(cart_seq.nextval, 'one', 1, 1);
select * from product;
select * from cart;


insert into orders(oseq, id) values(orders_seq.nextval, 'two');
insert into orders(oseq, id) values(orders_seq.nextval, 'one');
insert into orders(oseq, id) values(orders_seq.nextval, 'one');
insert into orders(oseq, id) values(orders_seq.nextval, 'two');

insert into order_detail(odseq, oseq, pseq, quantity)
values(order_detail_seq.nextval, 1, 1, 1);
insert into order_detail(odseq, oseq, pseq, quantity)
values(order_detail_seq.nextval, 1, 2, 5);
insert into order_detail(odseq, oseq, pseq, quantity)
values(order_detail_seq.nextval, 2, 4, 3);
insert into order_detail(odseq, oseq, pseq, quantity)
values(order_detail_seq.nextval, 2, 5, 2);
insert into order_detail(odseq, oseq, pseq, quantity)
values(order_detail_seq.nextval, 3, 3, 1);
insert into order_detail(odseq, oseq, pseq, quantity)
values(order_detail_seq.nextval, 3, 2, 1);
insert into order_detail(odseq, oseq, pseq, quantity)
values(order_detail_seq.nextval, 4, 6, 2);
insert into order_detail(odseq, oseq, pseq, quantity)
values(order_detail_seq.nextval, 4, 1, 2);



insert into qna (qseq, subject, content, id) 
values(qna_seq.nextval, '테스트', '질문내용1', 'one');

insert into qna (qseq, subject, content, id) 
values(qna_seq.nextval, '테스트2', '질문내용2', 'one');



create or replace view cart_view
as
select  c.cseq, c.id, m.name as mname, c.pseq, p.name as pname, 
	c.quantity, p.price2, c.result,  c.indate 
from cart c, product p, member m
where  c.pseq = p.pseq and m.id = c.id;


create or replace view order_view
as
select d.odseq, o.oseq, o.id, o.indate, d.pseq, d.quantity,  d.result, 
m.name as mname, m.zip_num, m.address, m.phone, 
p.name as pname,  p.price2
from orders o, order_detail d, member m, product p
where o.oseq = d.oseq and o.id = m.id and d.pseq = p.pseq;



create or replace view best_pro_view
as
select * from(
select rownum, pseq, name, price2, image 
from product  where bestyn='y'  order by indate desc
) where  rownum <=4;

create or replace view new_pro_view
as
select * from( 
select rownum, pseq, name, price2, image 
 from product  where useyn='y'  order by indate desc
 ) where  rownum <=4;

select * from best_pro_view
select * from new_pro_view

select * from address
select count(*) from address

select * from address where dong like '%대현동%';

select * from member
select * from cart
select * from cart_view
select * from order_view
-- SQL>@d:\zip.sql 

update order_detail set result='2' where oseq=14;



insert into qna (qseq, subject, content, id) 
values(qna_seq.nextval, '배송문의 입니다', 
	'언제 배송이 시작되고, 언제 오는지 알려주세요', 'scott');
	
insert into qna (qseq, subject, content, id) 
values(qna_seq.nextval, '교화 환불 문의 입니다', 
	'환불 교환은 배송비가 어떻게 되나요', 'scott');

	
update qna set reply='유선안내드리겠습니다', rep='2' where qseq=22

select concat(pseq, name) from product order by pseq desc

delete from product where pseq=16 or pseq=17 or pseq=18


update product set kind='4' where pseq=12;
update product set kind='5' where pseq=;


create table aaa(
	a int(4),
	b int(4)
);





insert into qna (qseq, subject, content, id) 
values(qna_seq.nextval, '배송문의 입니다', 
	'언제 배송이 시작되고, 언제 오는지 알려주세요', 'one');
insert into qna (qseq, subject, content, id) 
values(qna_seq.nextval, '교화 환불 문의 입니다', 
	'환불 교환은 배송비가 어떻게 되나요', 'one');
insert into qna (qseq, subject, content, id) 
values(qna_seq.nextval, '배송문의 입니다', 
	'언제 배송이 시작되고, 언제 오는지 알려주세요', 'two');
insert into qna (qseq, subject, content, id) 
values(qna_seq.nextval, '교화 환불 문의 입니다', 
	'환불 교환은 배송비가 어떻게 되나요', 'two');
insert into qna (qseq, subject, content, id) 
values(qna_seq.nextval, '배송문의 입니다', 
	'언제 배송이 시작되고, 언제 오는지 알려주세요', 'scott');
insert into qna (qseq, subject, content, id) 
values(qna_seq.nextval, '교화 환불 문의 입니다', 
	'환불 교환은 배송비가 어떻게 되나요', 'scott');	
	
	