# MAD MANAGER

---

## Outline

---

![ic_launcher_new](https://github.com/lucy1287/madcamp_task1/assets/80579765/e033e3f4-f108-42bd-8f77-84f2abef0531)

**MAD MANAGER에서는 동호회, 동아리 등에서 이루어진 스포츠 활동에 대한 여러 가지 정보들을 관리할 수 있습니다.**

**주소록을 연동하여 동아리 회원들의 정보들을 저장하거나, 경기 일정과 결과들을 관리하고 해당 경기의 사진이나 영상들을 함께 관리해보세요!**

## Team

---

[문재혁](https://www.notion.so/33b18ac1964b4a479b065ad57798aa83?pvs=21)

[이은재](https://www.notion.so/9a7e8d03723a4ffa91e83d24d3ced928?pvs=21)

## Tech Stack

---

**Front-End : KOTLIN**

**IDE : Android Studio**

## Preview

---

### TAB1

[KakaoTalk_20240703_174436459.mp4](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/60961290-3be6-4450-bc83-38ec6ebd0d74/KakaoTalk_20240703_174436459.mp4)

[KakaoTalk_20240703_162242172.mp4](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/cea8b25f-8ee2-49a9-88c9-89befd719f19/KakaoTalk_20240703_162242172.mp4)

### TAB2

[KakaoTalk_20240703_162241056.mp4](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/f0e98f20-1825-4933-b5da-8d57bf1ee090/KakaoTalk_20240703_162241056.mp4)

[KakaoTalk_20240703_173126483.mp4](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/b279942e-5027-4e43-9fba-22c5d3091793/KakaoTalk_20240703_173126483.mp4)

### TAB3

[KakaoTalk_20240703_170307852.mp4](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/8d0f5d2e-1325-4948-a60e-821e8a39c99a/KakaoTalk_20240703_170307852.mp4)

[KakaoTalk_20240703_170316538.mp4](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/012284e6-49a9-4d33-bc83-0a347fe3e714/KakaoTalk_20240703_170316538.mp4)

## Details

---

### MAIN

- `splash`를 사용하여 로딩 화면을 구현했습니다.
- 기본적으로 `TabLayout`을 이용하여 3개의 탭 사이를 이동할 수 있습니다.

### TAB1: Profiles

**연락처와 연동하여 각 인원들의 프로필을 조회하고 수정할 수 있는 탭**

- 내장된 주소록과 연동하여 `RecyclerView`를 통해 주소록의 인원들을 모두 표시해줍니다. 프로필을 담고 있는 각 `item` 에는 이름, 전화번호, 그룹 이름이 표시됩니다. 프로필의 전화 아이콘을 누르면 해당 프로필의 전화번호로 전화를 걸 수 있는 화면으로 넘어갑니다.
- `SearchView`를 통한 프로필 검색 기능을 지원합니다. 이름으로 프로필을 검색할 수 있습니다.
- 각 프로필을 클릭하면 프로필에 대한 정보를 담은 화면이 나옵니다. 이 화면에서 해당 프로필의 그룹 이름과 선수의 스탯을 지정해줄 수 있습니다.
- `RoomDB`를 이용하여 데이터베이스에 이름, 전화번호, 그룹 이름, 스탯이 저장되고 불러집니다. 프로필을 클릭하여 정보를 변경한 경우 데이터베이스에 새로운 정보가 업데이트 됩니다.

### TAB2: Sports Gallery

**스포츠 경기/연습 경기의 사진 및 동영상을 저장하고 볼 수 있는 탭**

- 새로운 경기/연습 경기를 추가하면 `RecyclerView`에 `CardView` 아이템이 추가됩니다
- 폴더 내부로 이동하여 내장 갤러리에서 사진/동영상을 불러올 수 있고 `RecyclerView`를 통해 `GridLayout`으로 표시됩니다
- 동영상의 경우 클릭 시 미디어 플레이어 화면으로 이동하여 해당 기능을 이용할 수 있습니다
- `RoomDB`를 이용하여 데이터베이스에 경기 제목, 경기 사진 및 동영상이 저장되고 불러집니다. 새로운 정보를 추가한 경우 데이터베이스에 새로운 정보가 업데이트 됩니다.

### TAB3: Calendar

**캘린더를 통해 스포츠 경기 일정과 참여 팀원을 관리할 수 있는 탭**

- `MaterialCalenderView`를 사용하여 캘린더에 경기 일정을 표시하고 관리할 수 있습니다.
- 캘린더의 특정 날짜를 클릭하면 경기 일정을 저장할 수 있는 화면이 표시됩니다. 이 화면에서 경기 제목, 경기 결과, 경기 참여 인원이 표시됩니다.
- `SearchView`를 통해 TAB1의 선수 명단에서 이름으로 검색할 수 있고, 해당 팀원들의 경기 참가 여부를 저장할 수 있습니다
- `RoomDB`를 이용하여 데이터베이스에 경기 제목, 경기 결과, 경기 참여 인원이 저장되고 불러집니다. 캘린더를 클릭하여 정보를 변경한 경우 데이터베이스에 새로운 정보가 업데이트 됩니다.
- 경기 일정을 저장하면 캘린더에 해당 경기의 제목과 함께 저장되었다는 `dot`이 표시됩니다



### Release
https://drive.google.com/file/d/1ScsaZmOLtKDU6bex_XsdABS-QdnCiyWZ/view?usp=sharing
