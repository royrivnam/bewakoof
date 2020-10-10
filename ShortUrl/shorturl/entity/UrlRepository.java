import org.springframework.data.repository.CrudRepository;

public interface UrlRepository extends CrudRepository<ShortUrl, Long>
{
    public ShortUrl findOneByHash(String hash);
}