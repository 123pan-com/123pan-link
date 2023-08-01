package main

import (
	"crypto/md5"
	"fmt"
	"log"
	"math/rand"
	"net/url"
	"time"
)

func SignURL(originURL, privateKey string, uid uint64, validDuration time.Duration) (newURL string, err error) {
	var (
		ts     = time.Now().Add(validDuration).Unix() // 有效时间戳
		rInt   = rand.Int()                           // 随机正整数
		objURL *url.URL
	)
	objURL, err = url.Parse(originURL)
	if err != nil {
		return "", err
	}
	authKey := fmt.Sprintf("%d-%d-%d-%x", ts, rInt, uid, md5.Sum([]byte(fmt.Sprintf("%s-%d-%d-%d-%s",
		objURL.Path, ts, rInt, uid, privateKey))))
	v := objURL.Query()
	v.Add("auth_key", authKey)
	objURL.RawQuery = v.Encode()
	return objURL.String(), nil
}

func main() {
	var (
		originURL            = "http://vip.123pan.com/29/音乐/02.一千零一夜-李克勤.wma" // 待签名URL，即用户在123云盘直链空间目录下复制的直链url
		privateKey           = "289ds32418bxdba"                              // 鉴权密钥，即用户在123云盘直链管理中设置的鉴权密钥
		uid           uint64 = 29                                             // 账号id，即用户在123云盘个人中心页面所看到的账号id
		validDuration        = time.Minute * 15                               // 链接签名有效期
	)
	newURL, err := SignURL(originURL, privateKey, uid, validDuration)
	if err != nil {
		log.Fatal(err)
	}
	fmt.Println(newURL)
}
