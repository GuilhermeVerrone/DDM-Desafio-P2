# MinhaLoja: produtos com API, Firebase Auth e Firestore

**Participantes:**

- Yasmin dos Santos Batista - RA: 241959
- Guilherme Verrone - RA: 216361
- Victoria Antunes - RA: 239588

**Disciplina:** Desenvolvimento para Dispositivos Móveis
**Professor(a):** ALESSANDRO FERREIRA PAZ DE LIMA

## Sobre o projeto

Aplicativo Android em Kotlin (layouts XML). O usuário entra com e-mail e senha, vê a lista de produtos da
[Fake Store API](https://fakestoreapi.com/products) e salva favoritos na nuvem.

| Tecnologia              | Uso                                                       |
| ----------------------- | --------------------------------------------------------- |
| Retrofit + Gson         | `GET /products` na Fake Store API                         |
| Firebase Authentication | Cadastro e login com e-mail e senha                       |
| Cloud Firestore         | Favoritos de cada usuário, em tempo real                  |
| Coil                    | Foto do produto                                           |

### Telas

1. **Login:** "Entrar" e "Criar conta", com mensagens de erro (senha errada, e-mail inválido, senha com menos de 6
   caracteres). Se já houver sessão, o app abre direto na lista de produtos.
2. **Produtos:** título, preço, categoria e foto de cada produto, com aviso de carregamento e de erro (com botão
   "Tentar de novo"). Tocar num produto o salva nos favoritos, e o coração fica cheio nos que já são favoritos.
   Botões **Meus favoritos** e **Sair**.
3. **Meus favoritos:** lista em tempo real (`addSnapshotListener`), do mais recente para o mais antigo, com
   lixeira para remover.

## Estrutura no Firestore

```
usuarios (coleção)
└── <uid> (documento)
    └── favoritos (subcoleção)
        └── <id do produto> (documento) ─ id, titulo, preco, categoria, salvoEm (serverTimestamp)
```

O id do produto é o id do documento, então salvar duas vezes não duplica. As regras de
[`firestore.rules`](firestore.rules) só deixam cada usuário ler e gravar em `usuarios/{seu uid}`.

## Organização do código

```
app/src/main/java/com/example/minhaloja/
├── model/  Produto, Rating (API) e Favorito (Firestore)
├── data/   ProdutoService, RetrofitClient, AuthRepository, FavoritoRepository
└── view/   LoginActivity, ProdutosActivity, FavoritosActivity, adapters e UiExt
```

## Dependências

| Dependência                                | Versão          |
| ------------------------------------------ | --------------- |
| `com.google.firebase:firebase-bom`         | 34.0.0          |
| `firebase-auth`, `firebase-firestore`      | (do BoM)        |
| `com.squareup.retrofit2:retrofit`          | 2.11.0          |
| `com.squareup.retrofit2:converter-gson`    | 2.11.0          |
| `io.coil-kt:coil`                          | 2.7.0           |
| `androidx.lifecycle:lifecycle-runtime-ktx` | 2.6.2           |
| `com.google.android.material:material`     | 1.10.0          |
| `androidx.appcompat`, `activity-ktx`, `core-ktx`, `recyclerview`, `constraintlayout` | ver `gradle/libs.versions.toml` |

Plugins: Android Gradle Plugin 8.5.1, Kotlin 2.0.21 e `com.google.gms.google-services` 4.5.0. O Gradle 8.7 vem
pelo wrapper. Use um JDK de 17 a 21 (o do Android Studio serve) e o Android SDK 34.

## Como rodar

1. No [Console do Firebase](https://console.firebase.google.com), crie o projeto **MinhaLoja** e adicione um
   app Android com o pacote `com.exemple.minhaloja`.
2. Baixe o `google-services.json` e coloque em `app/` (está no `.gitignore`; há um modelo em
   `app/google-services.json.example`).
3. Em **Authentication → Método de login**, ative **E-mail/senha**.
4. Em **Firestore Database**, crie o banco e publique as regras de [`firestore.rules`](firestore.rules)
   (ou `firebase deploy --only firestore:rules`).
5. Abra no Android Studio e execute.
