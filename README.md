ROVSHAN ALKHANOV 

Unitech application qısa zaman və sıx qrafik ərzində hazırlanmışdır. Bu səbəbdən bəzi şeylər unudulmuş ola bilər,
bəzi xətalar, bəzi yanlışlıqlar ola bilər, qaçınılmazdır. Lakin bu zaman kəsiyində 2 full time işlə paralel tələb 
olunanları etməyə çalışdım.

İlk növbədə tələblərdə olmadığı halda postman collection və environment hazır etdim ki, bütün apilər rahatlıqla 
test edilə bilsin.

Baza olaraq MySql istifadə etdim, rahat hesab edirəm, Postgre də istifadə edilə bilərdi, lakin Postgre hazırki 
komputerimdə niyəsə düzgün çalışmır, hətta docker-compose.file vasitəsilə belə. Bu səbəbdən MySql istifadə etdim.

Register Login sadə formada tələb olunmuşdu, lakin mən token vasitəsilə etdim, ancaq access token implementasiya etdim,
refresh də edilə bilərdi lakin, çox mürəkkəbləşdirmədim.

Tələblərdə Account yaradılması Api yox idi ancaq Get Accounts var idi, şəxsi improvizasiyam ilə yaradılması ilə bağlı 
api yazdım və tokendən userid götürüb həmin accounta map etdim. Login olan user öz accpountlarını görür.
(Yaradıldıqda hamısı active olaraq yaradılıb,save edilir, deactive üçün api yazmaq ya da account yaradıldıqda
request bodydə götürmək olar),

Get Accounts ancaq status active olan accountları fetch edir, bunu @Query və ya Criteria və s. kimi üsullar ilə də etmək 
olardı lakin mən bütün accountları fetch edir service də statusu filter edir, bunun performans olaraq böyük datalar üçün 
problem ola biləcəyini bilirəm amma vurğulamaq üçün bu tipdə etdim. Düzgün olan üsulun Bazadan full data çəkmək yox, 
ancaq filterə uyğun dataları çəkmək olduğunu bilirəm.

Transfer servisini tələbə uyğun və müəyyən qədər şəxsi improvizasiyam ilə hazırladım. Orda əlavə olaraq, 
@Transactional ilə bağlı nəsə etmək olar mən sadə istifadə etdim.

Currency ilə bağlı api sadədir, performans ilə bağlı cache istifadəsini uyğun gördüm, L2 cache istifadə etmək olardı 
məsələn Hazelcast və ya başqası. Redis istifadəsi də uyğun olardı lakin zamanı və mürəkkəbliyi nəzərə alıb, datanı 
ConcurrentHashMapa yığdım ora ilə işlədim third party 1 dəqiqədən bir update nəzərə alıb 59 saniyədən bir cache evict edirəm
digər adı ilə Mapi clear edirəm. Cache ə əlavə olunan data əlavə olunmasından 59 saniyə sonra silinməsə user datanı 
cachedən oxusa dirty data oxumuş ola bilər. 


UnitTestlər üçün zamanım çox az qaldı əlimdən gələn qədər yazdım, test first yanaşma edilsə daha yaxşı unittestlər olardı, 
lakin mən taskı bitirib unittestləri sonra yazdım.

Bəzi yerlərdə comment yazmışam. Vaxta görə tam fikir vermədim.

Mapping üçün əsasən Mapstruct istifadə edirəm, bu proyektdə isə Builder və get-set istifadə etdim.

