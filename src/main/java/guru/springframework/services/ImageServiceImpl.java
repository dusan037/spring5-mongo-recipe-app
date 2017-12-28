package guru.springframework.services;

import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.reactive.RecipeReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by jt on 7/3/17.
 */
@Slf4j
@Service
public class ImageServiceImpl implements ImageService {


    private final RecipeReactiveRepository recipeReactiveRepository;

    public ImageServiceImpl( RecipeReactiveRepository recipeReactiveRepository) {

        this.recipeReactiveRepository = recipeReactiveRepository;
    }

    @Override
    public Mono<Void> saveImageFile(String recipeId, MultipartFile file) {

        Mono<Recipe> recipeMono = recipeReactiveRepository.findById(recipeId).map(recipe -> {
            try {
                Byte[] bytteObjects = new Byte[file.getBytes().length];

                int i = 0;

                for (byte b : file.getBytes()) {
                    bytteObjects[i++] = b;
                }

                recipe.setImage(bytteObjects);

                return recipe;
            } catch (IOException ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }});

        recipeReactiveRepository.save(recipeMono.block());

        return Mono.empty();
    }
}
