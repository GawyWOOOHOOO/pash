rm -rf 1
mkdir -p 1
mkdir -p /dev/shm/dish
rm -f "#file11"
mkfifo "#file11"
rm -f "#file14"
mkfifo "#file14"
rm -f "#file15"
mkfifo "#file15"
rm -f "#file17"
mkfifo "#file17"
rm -f "#file12"
mkfifo "#file12"
rm -f "#file13"
mkfifo "#file13"
rm -f "#file18"
mkfifo "#file18"
rm -f "#file10"
mkfifo "#file10"
rm -f "#file20"
mkfifo "#file20"
rm -f "#file22"
mkfifo "#file22"
rm -f "#file21"
mkfifo "#file21"
rm -f "#file16"
mkfifo "#file16"
rm -f "#file19"
mkfifo "#file19"
cat ${IN} > "#file11" &
cat ${IN} > "#file12" &
cat ${IN} > "#file13" &
cat ${IN} > "#file14" &
cat "#file11" | tr A-Z a-z > "#file15" &
cat "#file12" | tr A-Z a-z > "#file16" &
cat "#file13" | tr A-Z a-z > "#file17" &
cat "#file14" | tr A-Z a-z > "#file18" &
cat "#file15" | sort  > "#file19" &
cat "#file16" | sort  > "#file20" &
cat "#file17" | sort  > "#file21" &
cat "#file18" | sort  > "#file22" &
sort -m --parallel=4 "#file19" "#file20" "#file21" "#file22" > "#file10" &
cat "#file10" > 1/0 &
wait
rm -f "#file11"
rm -f "#file14"
rm -f "#file15"
rm -f "#file17"
rm -f "#file12"
rm -f "#file13"
rm -f "#file18"
rm -f "#file10"
rm -f "#file20"
rm -f "#file22"
rm -f "#file21"
rm -f "#file16"
rm -f "#file19"
rm -rf "/dev/shm/dish"