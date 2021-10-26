/* 사용자정보 */
create table user_info(
	user_id varchar(100) not null				/* 아이디 = 닉네임 */
  , user_pwd varchar(20) not null				/* 비밀번호 */
  , user_token mediumtext						/* 토큰 */
  , reg_date timestamp							/* 등록일시 */
  , primary key(user_id)
) default charset=utf8 collate utf8_general_ci;


/* 택시 메이트 등록정보 */
create table boarding_board(
	board_no int not null auto_increment		/* 게시글 번호 */
  , writer varchar(100) not null				/* 작성자 - 그룹장 */
  , start_area varchar(200)						/* 출발지 */
  , start_x mediumtext							/* 출발지 x 좌표 */
  , start_y mediumtext							/* 출발지 y 좌표 */
  , end_area varchar(200)						/* 도착지 */	
  , end_x mediumtext							/* 도착지 x 좌표 */
  , end_y mediumtext							/* 도착지 y 좌표 */
  , content mediumtext							/* 게시글 */
  , boarding_date varchar(12)					/* 탑승일시 */
  , reg_date timestamp							/* 등록일시 */
  , primary key(board_no)
) default charset=utf8 collate utf8_general_ci;


/* 택시 메이트 동승자 정보 */
create table boarding_user(
	board_no int not null						/* 게시글 번호 */
  , user_id varchar(100) not null				/* 아이디 */
  , leader_yn varchar(1) 						/* 그룹장 여부 - 'Y' : 그룹장 */
  , primary key(board_no, user_id)
) default charset=utf8 collate utf8_general_ci;


/* 택시 탑승 완료시 운행정보 */
create table boarding_board_fixed(
	board_no int not null						/* 게시글 번호 */
  , expect_time	int								/* 예상시간(분) */
  , boarding_cnt int							/* 탑승인원 */
  , expect_pay int								/* 예상금액 */
  , expect_pay_one int							/* 개인부담금액 */
  , primary key(board_no)
) default charset=utf8 collate utf8_general_ci;

select * from boarding_user bu where board_no = 10;

/* 택시 운행 종료 정보 */
create table boarding_push(
	board_no int not null						/* 게시글 번호 */
  , pay int										/* 운행요금 */
  , dutch_pay int								/* 더치페이 금액 */
  , bank_name varchar(10)						/* 은행명 */
  , bank_no varchar(20)							/* 계좌번호 */
  , push_yn varchar(1)							/* 푸시알림여부 */
  , reg_date timestamp							/* 등록일자 */
  , primary key(board_no)
) default charset=utf8 collate utf8_general_ci;

/* 신고내역 */
create table blacklist_user(
	board_no int								/* 게시글번호 */
  , user_id varchar(100)						/* 아이디 */
  , primary key(board_no, user_id)									
) default charset=utf8 collate utf8_general_ci;
