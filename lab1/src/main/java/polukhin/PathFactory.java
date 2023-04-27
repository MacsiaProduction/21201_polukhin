package polukhin;

import polukhin.exceptions.FileMissingException;
import polukhin.exceptions.PathFactoryException;
import polukhin.modules.DuCompoundFileType;
import polukhin.modules.DuFileType;
import polukhin.modules.MetaType;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.*;

public class PathFactory {
    private final List<MetaType<? extends DuFileType>> metaTypes;

    public PathFactory(List<Class<? extends MetaType<? extends DuFileType>>> classList) throws PathFactoryException {
        this.metaTypes = new ArrayList<>();
        for (Class<? extends MetaType<? extends DuFileType>> clazz : classList) {
            try {
                this.metaTypes.add(clazz.getConstructor().newInstance());
            } catch (Exception e) {
                throw new PathFactoryException("Exception while instantiation of metaTypes");
            }
        }
    }

    private DuFileType create(Path path, JduOptions jduOptions) throws PathFactoryException {
        var metaType = getMetaOf(path);
        if (metaType == null) {
            throw new PathFactoryException("Path" + path + "can't be recognized as any type");
        } else try {
            Class<? extends DuFileType> clazz = metaType.getFileType();
            Constructor<? extends DuFileType> constructor = clazz.getConstructor(Path.class, JduOptions.class);
            return constructor.newInstance(path, jduOptions);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            throw new PathFactoryException("Error creating instance of class " + metaType);
        }
    }

    public DuFileType buildTree(Path root, JduOptions options) throws PathFactoryException, FileMissingException {
        List<List<DuFileType>> layers = new ArrayList<>();
        var initialDir = create(root, options);
        layers.add(List.of(initialDir));
        for (int i = 1; i < options.depth() && layers.get(i-1).size() != 0; i++) {
           layers.add(initNextLayer(layers.get(i-1), options));
        }
        for (int i = layers.size() - 1; i >= 0; i--) {
            calculateLayer(layers.get(i));
        }
        return initialDir;
    }

    private List<DuFileType> initNextLayer(List<DuFileType> layer, JduOptions options) throws FileMissingException, PathFactoryException {
        List<DuFileType> nextLayer = new ArrayList<>();
        for (var element: layer) {
            if(element instanceof DuCompoundFileType) {
                var childrenAsPaths = ((DuCompoundFileType) element).getChildrenAsPaths();
                List<DuFileType> childrenAsTypes = new ArrayList<>();
                Iterator<Path> iterator = childrenAsPaths.iterator();
                while(iterator.hasNext()) {
                    Path path = iterator.next();
                    var duFile = create(path, options);
                    nextLayer.add(duFile);
                    childrenAsTypes.add(duFile);
                }
                ((DuCompoundFileType) element).setChildren(childrenAsTypes);
            }
        }
        return nextLayer;
    }

    private void calculateLayer(List<DuFileType> layer) throws FileMissingException {
        for(var element : layer) {
            var meta = getMetaOf(element.path());
            assert meta != null;
            meta.calculateSize(element);
        }
    }

    private MetaType<? extends DuFileType> getMetaOf(Path path) {
        for (var metaType : metaTypes) {
            if (metaType.isCompatible(path)) {
                return metaType;
            }
        }
        return null;
    }
}