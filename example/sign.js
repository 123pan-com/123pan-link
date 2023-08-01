const crypto = require('crypto-js');

function SignURL(originURL, privateKey, uid, validDuration) {
  const ts = Math.floor((Date.now() + validDuration) / 1000); // 有效时间戳，单位：秒
  const rInt = Math.floor(Math.random() * 1000000); // 随机正整数，这里生成一个最大值为1000000的随机整数
  const objURL = new URL(originURL);
  const authKey = `${ts}-${rInt}-${uid}-${crypto
    .MD5(`${decodeURIComponent(objURL.pathname)}-${ts}-${rInt}-${uid}-${privateKey}`)
    .toString()}`;
  objURL.searchParams.append('auth_key', authKey);
  return objURL.toString();
}


function testSign(){
	const originURL = 'http://vip.123pan.com/10/layout1/layout2/%E6%88%91.txt'; // 待签名URL
	const privateKey = 'mykey'; // 鉴权密钥
	const uid = 10; // 账号id
	const validDuration = 15 * 60 * 1000; // 链接签名有效期，单位：毫秒

	const newURL = SignURL(originURL, privateKey, uid, validDuration);
	console.log(newURL);
}

